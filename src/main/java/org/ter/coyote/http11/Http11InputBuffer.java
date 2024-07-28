package org.ter.coyote.http11;

import org.ter.coyote.InputBuffer;
import org.ter.coyote.CoyoteRequest;
import org.ter.util.buf.CharsetFunctions;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public class Http11InputBuffer implements InputBuffer {
    protected CoyoteRequest request;
    protected SocketWrapperBase<?> socketWrapper;
    private ByteBuffer byteBuffer;
    private volatile boolean parsingRequestLine;
    private final int headerBufferSize;
    private byte chr = Constants.ZERO;
    private byte prev = Constants.ZERO;
    public Http11InputBuffer(CoyoteRequest request, int headerBufferSize) {
        this.request = request;
        this.parsingRequestLine = true;
        this.headerBufferSize = headerBufferSize;
    }

    /**
     * 初始化缓冲区
     *
     * @param socketWrapper 缓冲区需要关联的SocketWrapper对象
     */
    public void init(SocketWrapperBase<?> socketWrapper) {
        this.socketWrapper = socketWrapper;
        int bufLength = headerBufferSize + socketWrapper.getSocketBufferHandler().getReadBuffer().capacity();
        if (Objects.isNull(byteBuffer) || byteBuffer.capacity() < bufLength) {
            byteBuffer = ByteBuffer.allocate(bufLength);
            byteBuffer.position(0).limit(0);
        }
    }
    /**
     * 读取Http请求的请求行，不能使用这个方法来读取Http请求的正文
     *
     * @return true表示有数据被读取， false没有数据被读取，需要释放线程
     * @throws IOException 在读取 HTTP 请求行过程当中发生I/O错误
     */
    public boolean parseRequestLineSplit() throws IOException {
        if (!parsingRequestLine) {
            return true;
        }
        if (!skipStartCRLF()) return false;
        try {
            // 获取请求行
            String line = readStringLine();
            String[] split = line.split(Constants.SP_STR,3);
            int querySplitIndex = split[1].indexOf(Constants.QUESTION);
            if(split.length != 3){
                return false;
            }
            // 请求路径携带参数
            if(querySplitIndex > 0){
                request.setStrVal(CoyoteRequest.Type.URI_MB, split[1].substring(0, querySplitIndex));
                request.setStrVal(CoyoteRequest.Type.QUERY_MB, split[1].substring(querySplitIndex + 1));
            }else{
                request.setStrVal(CoyoteRequest.Type.URI_MB, split[1]);
            }
            request.setStrVal(CoyoteRequest.Type.METHOD_MB, split[0]);
            request.setStrVal(CoyoteRequest.Type.PROTO_MB, split[2]);
        }finally {
            parsingRequestLine = false;
        }
        return true;
    }

    private boolean skipStartCRLF() throws IOException {
        do {
            if (!read()) {
                return false;
            }
            if (request.getStartTime() < 0) {
                request.setStartTime(System.currentTimeMillis());
            }
            chr = byteBuffer.get();
        } while (chr == Constants.CR || chr == Constants.LF);
        byteBuffer = byteBuffer.position(byteBuffer.position() - 1);
        return true;
    }
    private String readStringLine(){
        ByteBuffer buffer = readLine(byteBuffer);
        return Objects.isNull(buffer) ? null : CharsetFunctions.stringAscii(buffer.array(), 0, buffer.limit());
    }
    private ByteBuffer readLine(ByteBuffer buffer){
        ByteBuffer allocate = ByteBuffer.allocate(buffer.remaining());
        while (buffer.hasRemaining()){
            prev = chr;
            chr = buffer.get();
            allocate.put(chr);
            if((prev== Constants.CR && chr == Constants.LF)){
                allocate.limit(allocate.position()-2);
                allocate.position(0);
                return allocate;
            }
        }
        // 避免因为没有完整的 "CRLF" 而无法读取最后一行
        if (allocate.position() > 0) {
            allocate.limit(allocate.position());
            allocate.position(0);
            return allocate;
        }
        return null;
    }
    @Override
    public boolean read() throws IOException {
        int nRead;
        int mark = byteBuffer.position();
        try {
            nRead = socketWrapper.read(byteBuffer);
        } finally {
            if (mark <= byteBuffer.position()) {
                byteBuffer.limit(byteBuffer.position()).position(mark);
            } else {
                byteBuffer.limit(0).position(0);
            }
        }
        return nRead > 0;
    }

    /**
     * 解析 HTTP 请求头
     * @return true解析成功，false 解析失败
     * @throws IOException 在解析过程当中发生I/O错误
     */
    public boolean parseHeaders() {
        String line;
        while (Objects.nonNull(line = readStringLine()) && !line.isEmpty() && line.getBytes().length != 0){
            String[] pair = line.split(Constants.COLON_STR, 2);
            if(pair.length !=2){
                return false;
            }
            if(request.hasFieldValue(pair[0])){
                request.putHeader(pair[0],
                        request.getFieldValue(pair[0] +Constants.SEMI_COLON_STR+pair[1].replaceFirst(Constants.CARET_PLUS,Constants.EMPTY)));
            }else {
                request.putHeader(pair[0], pair[1].replaceFirst(Constants.CARET_PLUS, Constants.EMPTY));
            }
        }
        return true;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }
}