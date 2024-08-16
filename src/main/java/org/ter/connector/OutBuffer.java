package org.ter.connector;

import org.ter.coyote.CoyoteResponse;
import org.ter.util.buf.C2BConverter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

public class OutBuffer extends Writer {
    private C2BConverter converter = new C2BConverter();
    public static final int DEFAULT_BUFFER_SIZE = 8 * 1024;
    private CoyoteResponse coyoteResponse;
    private CharBuffer charBuffer;
    private ByteBuffer byteBuffer;
    private boolean initial = true;
    private boolean closed = false;
    public OutBuffer(){
        this(DEFAULT_BUFFER_SIZE);
    }
    public OutBuffer(int size){
        charBuffer = CharBuffer.allocate(size);
        clear(charBuffer);
        byteBuffer = ByteBuffer.allocate(size);
        clear(byteBuffer);
    }
    public CoyoteResponse getCoyoteResponse() {
        return coyoteResponse;
    }
    public void setCoyoteResponse(CoyoteResponse coyoteResponse) {
        this.coyoteResponse = coyoteResponse;
    }
    @Override
    public void write(char[] buff, int off, int len) throws IOException {
        append(buff, off, len);
    }
    @Override
    public void flush() throws IOException {
        if(initial){
            coyoteResponse.sendHeaders();
            initial = false;
        }
//        if(charBuffer.remaining() > 0){
//            flushCharBuffer();
//        }
//        if(byteBuffer.remaining() > 0){
//            flushByteBuffer();
//        }
        closed = true;
    }
    /**
     * 关闭输出缓冲区。如果响应尚未提交，将计算响应的大小，
     * 将其写入ByteBuffer缓冲区，进行提交返回
     *
     * @throws IOException  在此过程当中发生I/O错误
     */
    @Override
    public void close() throws IOException {
        // 当对应的HttpServlet处理完请求之后，将响应的数据写入CharBuffer缓冲区，
        // 这里判断CharBuffer缓冲区当中是否还有数据没有返回，如果有数据没有写入
        // ByteBuffer字节缓冲区，就将CharBuffer缓冲区中的数据写入ByteBuffer字节
        // 缓冲区。
        // 这个操作一般发生在HttpServlet处理完请求，关闭输出缓冲区时。
        if(charBuffer.remaining() > 0){
            flushCharBuffer();
        }
        if ((!coyoteResponse.isCommitted()) && (coyoteResponse.getContentLengthLong() == -1) &&
                !coyoteResponse.getRequest().getMethodMB().equals("HEAD")) {
            coyoteResponse.setContentLength(byteBuffer.remaining());
        }
        flush();
    }

    /**
     * 将字符数据转换为byte形式，将其返回给发起此次请求的客户端
     * @param from 需要写入到响应的字符缓冲区
     */
    private void realWriteChars(CharBuffer from) throws IOException {
        if (from.remaining() > 0){
            converter.convert(from, byteBuffer);
        }
    }

    /**
     * 将缓冲区的数据发送给请求的客户端
     *
     * @param buffer 需要发送到请求客户端的数据
     */
    private void realWriteBytes(ByteBuffer buffer){
        if(closed){
            return;
        }
        try {
            if (buffer.remaining() > 0) {
                coyoteResponse.doWrite(buffer);
            }
        }catch (IOException exception){
            // 记录日志
        }
    }
    private void clear(Buffer buffer) {
        buffer.rewind().limit(0);
    }
    /**
     * 将字符数据添加到字符缓冲区
     * @param chars 需要添加到缓冲区的字符数据
     * @param off   开始位置
     * @param len 需要添加的字符长度
     */
    public void append(char[] chars, int off, int len) {
        charBuffer.mark().position(charBuffer.limit()).limit(charBuffer.capacity());
        charBuffer.put(chars, off, len);
        charBuffer.limit(charBuffer.position()).reset();
    }

    /**
     * 将Byte数据写入到字节缓冲区当中
     * @param bytes 需要添加到字节缓冲区的数据
     * @param off 开始位置
     * @param len 需要添加的字节长度
     */
    public void append(byte[] bytes, int off, int len){
        byteBuffer.mark().position(byteBuffer.limit()).limit(byteBuffer.capacity());
        byteBuffer.put(bytes, off, len);
        byteBuffer.limit(byteBuffer.position()).reset();
    }
    private void flushByteBuffer() {
        realWriteBytes(byteBuffer.slice());
        clear(byteBuffer);
    }
    private void flushCharBuffer() throws IOException {
        realWriteChars(charBuffer.slice());
        clear(charBuffer);
    }

    public void setBufferSize(int bufferSize){
        if(bufferSize > byteBuffer.capacity()){
            byteBuffer = ByteBuffer.allocate(bufferSize);
            byteBuffer.clear();
        }
    }
    public int getBufferSize(){
        return byteBuffer.capacity();
    }
    public void reset(){
        clear(byteBuffer);
        clear(charBuffer);

        initial = true;
        closed = false;
    }
}
