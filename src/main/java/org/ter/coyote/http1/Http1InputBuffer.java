package org.ter.coyote.http1;

import org.ter.coyote.InputBuffer;
import org.ter.coyote.Request;
import org.ter.coyote.http1.filter.InputFilter;
import org.ter.coyote.http1.filter.OutputFilter;
import org.ter.util.buf.CharsetFunctions;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public class Http1InputBuffer implements InputBuffer {
    protected Request request;
    protected SocketWrapperBase<?> socketWrapper;
    protected OutputFilter[] filterLibrary;
    protected OutputFilter[] activeFilters;
    private int lastFilter;
    private ByteBuffer byteBuffer;
    private volatile boolean parsingRequestLine;
    private int parsingRequestLineStart;
    private final int headerBufferSize;
    private byte chr;

    public Http1InputBuffer(Request request, int headerBufferSize) {
        this.request = request;
        this.filterLibrary = new InputFilter[0];
        this.activeFilters = new InputFilter[0];
        this.lastFilter = -1;
        this.parsingRequestLine = true;
        this.headerBufferSize = headerBufferSize;
        this.chr = 0;
        this.parsingRequestLineStart = 0;
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
        if (!f()) return false;
            
        boolean space = false;
        int pos = 0;
        while (!space) {
            pos = byteBuffer.position();
            chr = byteBuffer.get();
            if (chr == Constants.CR || chr == Constants.LF) {
                space = true;
            }
        }
        try {
            // 获取请求行
            String line = readLine(byteBuffer, 0, pos - parsingRequestLineStart);
            String[] split = line.split(Constants.SPLIT,3);
            int querySplitIndex = split[1].indexOf(Constants.QUESTION);
            if(split.length != 3){
                return false;
            }
            // 请求路径携带参数
            if(querySplitIndex > 0){
                request.setStrVal(Request.Type.URI_MB, split[1].substring(0, querySplitIndex));
                request.setStrVal(Request.Type.QUERY_MB, split[1].substring(querySplitIndex + 1));
            }else{
                request.setStrVal(Request.Type.URI_MB, split[1]);
            }
            request.setStrVal(Request.Type.METHOD_MB, split[0]);
            request.setStrVal(Request.Type.PROTO_MB, split[2]);
        }finally {
            parsingRequestLine = false;
            parsingRequestLineStart = byteBuffer.position();
        }
        return true;
    }

    private boolean f() throws IOException {
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
        parsingRequestLineStart = byteBuffer.position();
        return true;
    }
    private String readLine(ByteBuffer buffer, int start, int end){
        return Objects.isNull(buffer) ? null : CharsetFunctions.stringAscii(buffer.array(), start, end);
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
    public boolean parseHeaders() throws IOException{
        return true;
    }
}
