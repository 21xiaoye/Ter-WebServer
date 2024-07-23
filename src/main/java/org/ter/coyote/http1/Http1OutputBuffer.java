package org.ter.coyote.http1;

import org.ter.coyote.OutputBuffer;
import org.ter.coyote.Response;
import org.ter.coyote.http1.filter.OutputFilter;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Http1OutputBuffer implements OutputBuffer {
    protected Response response;

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

    /**
     * 响应正文的过滤器
     */
    protected OutputFilter[] filterLibrary;
    protected OutputFilter[] activeFilters;
    /**
     * 最后一个过滤器标志
     */
    private int lastFilter;
    public Http1OutputBuffer(Response response, int heardOutputBufferSize){
        this.response = response;
        headerBuffer = ByteBuffer.allocate(heardOutputBufferSize);
        filterLibrary = new OutputFilter[0];
        activeFilters = new OutputFilter[0];
        socketOutputBuffer = new SocketOutputBuffer();
        this.responseFinished = false;
        lastFilter = -1;
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
}
