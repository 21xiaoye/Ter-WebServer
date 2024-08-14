package org.ter.coyote.http11;

import org.ter.coyote.CoyoteResponse;
import org.ter.coyote.OutputBuffer;
import org.ter.util.Constants;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

public class Http11OutputBuffer implements OutputBuffer {
    protected CoyoteResponse response;

    protected SocketWrapperBase<?> socketWrapper;
    protected OutputBuffer socketOutputBuffer;
    /**
     * 用于返回响应标头的缓冲区
     */
    protected final ByteBuffer headerBuffer;
    /**
     * 标记是否已完成响应
     */
    protected boolean responseFinished;

    public Http11OutputBuffer(CoyoteResponse response, int heardOutputBufferSize){
        this.response = response;
        headerBuffer = ByteBuffer.allocate(heardOutputBufferSize);
        socketOutputBuffer = new SocketOutputBuffer();
        this.responseFinished = false;
    }
    @Override
    public int doWrite(ByteBuffer buffer) throws IOException {
        return socketOutputBuffer.doWrite(buffer);
    }

    /**
     * 输出缓冲区，主要作用是将数据写入Socket
     */
    public class SocketOutputBuffer implements OutputBuffer{
        @Override
        public int doWrite(ByteBuffer buffer) throws IOException {
            int len = buffer.remaining();
            socketWrapper.write(buffer);
            return len;
        }
    }
    public void init(SocketWrapperBase<?> socketWrapper){
        this.socketWrapper = socketWrapper;
    }

    /**
     * 返回状态行信息，例如：HTTP1.1 200 OK\r\n,
     */
    public void sendStatusLine(){
        write(Constants._HTTP11_BYTES);
        headerBuffer.put(Constants.SP);
        int status = response.getStatus();
        write(status);
        headerBuffer.put(Constants.SP);
        String message = response.getMessage();
        if(Objects.isNull(message)){
            write("OK");
        }else{
            write(message);
        }
        headerBuffer.put(Constants._CRLF_BYTES);
    }

    /**
     * 返回响应标头信息，标准的标头格式为 Connection: keep-alive\r\n
     *
     * @param name 响应头名称
     * @param value 响应头值
     */
    public void sendHeader(String name, String value){
        write(name);
        headerBuffer.put(Constants.COLON).put(Constants.SP);
        write(value);
        headerBuffer.put(Constants.CR).put(Constants.LF);
    }
    public void endHeaders() {
        headerBuffer.put(Constants.CR).put(Constants.LF);
    }
    private void write(int value){
        String str = Integer.toString(value);
        for (int i =0; i<str.length();i++){
            byte ch =  (byte)str.charAt(i);
            headerBuffer.put(ch);
        }
    }
    /**
     * 将给定的字节数据写入到输出流当中去，
     * 用户响应状态行的写入返回
     *
     * @param bytes 需要写入的字节数据
     */
    private void write(byte[] bytes){
        headerBuffer.put(bytes);
    }
    /**
     * 此方法将给定的字符串写入到输出流当中去，
     * 用于响应标头的写入返回
     *
     * @param str 需要写入的标头信息
     */
    private void write(String str){
        byte[] buffer = str.getBytes();
        for (int i = 0; i < buffer.length; i++) {
            if ((buffer[i] > -1 && buffer[i] <= 31 && buffer[i] != 9) || buffer[i] == 127) {
                buffer[i] = ' ';
            }
        }
        headerBuffer.put(buffer);
    }
    protected void commit() throws IOException{
        response.setCommitted(true);
        if(headerBuffer.position() > 0){
            headerBuffer.flip();
            try {
                socketWrapper.write(headerBuffer);
            }finally {
                headerBuffer.clear();
            }
        }
    }
}
