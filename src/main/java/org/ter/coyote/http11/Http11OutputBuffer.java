package org.ter.coyote.http11;

import org.ter.coyote.CoyoteResponse;
import org.ter.coyote.OutputBuffer;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Http11OutputBuffer implements OutputBuffer {
    protected CoyoteResponse response;

    protected SocketWrapperBase<?> socketWrapper;
    protected OutputBuffer socketOutputBuffer;
    /**
     * 用于标头的缓冲区
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
        return 0;
    }

    /**
     * 输出缓冲区，主要作用是将数据写入Socket
     */
    public class SocketOutputBuffer implements OutputBuffer{
        @Override
        public int doWrite(ByteBuffer buffer) throws IOException {
            return 0;
        }
    }
    public void init(SocketWrapperBase<?> socketWrapper){
        this.socketWrapper = socketWrapper;
    }
}
