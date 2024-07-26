package org.ter.coyote.http1;

import org.ter.coyote.InputBuffer;
import org.ter.coyote.Request;
import org.ter.coyote.http1.filter.InputFilter;
import org.ter.coyote.http1.filter.OutputFilter;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.io.EOFException;
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
    private final int headerBufferSize;

    public Http1InputBuffer(Request request,int headerBufferSize){
        this.request = request;
        this.filterLibrary = new InputFilter[0];
        this.activeFilters = new InputFilter[0];
        this.lastFilter = -1;
        this.parsingRequestLine = true;
        this.headerBufferSize = headerBufferSize;
    }
    public void init(SocketWrapperBase<?> socketWrapper){
        this.socketWrapper = socketWrapper;
        int bufLength = headerBufferSize + socketWrapper.getSocketBufferHandler().getReadBuffer().capacity();
        if(Objects.isNull(byteBuffer) || byteBuffer.capacity() < bufLength){
            byteBuffer = ByteBuffer.allocate(bufLength);
            byteBuffer.position(0).limit(0);
        }
    }

    /**
     * 读取Http请求的请求行，不能使用这个方法来读取Http请求的正文
     * @return true表示有数据被读取， false没有数据被读取，需要释放线程
     *
     * @throws IOException 在读取 HTTP 请求行过程当中发生I/O错误
     */
    boolean parseRequestLine() throws IOException {
        if(!parsingRequestLine){
            return true;
        }
        if(!read()){
            return false;
        }
        return true;
    }
    private boolean read() throws IOException {
        int mark = byteBuffer.position();
        int nRead;
        try {
            if (byteBuffer.position() < byteBuffer.limit()) {
                byteBuffer.position(byteBuffer.limit());
            }
            byteBuffer.limit(byteBuffer.capacity());
            nRead = socketWrapper.read(byteBuffer);
        }finally {
            if (byteBuffer.position() >= mark) {
                byteBuffer.limit(byteBuffer.position());
                byteBuffer.position(mark);
            } else {
                byteBuffer.position(0);
                byteBuffer.limit(0);
            }
        }
        if (nRead > 0) {
            System.out.println(nRead);
            return true;
        } else {
            return false;
        }
    }
}
