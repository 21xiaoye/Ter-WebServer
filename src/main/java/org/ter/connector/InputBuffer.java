package org.ter.connector;

import org.ter.coyote.CoyoteRequest;
import org.ter.coyote.CoyoteResponse;

import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class InputBuffer extends Reader {
    public static final int DEFAULT_BUFFER_SIZE = 8 * 1024;
    private CoyoteRequest coyoteRequest;
    private ByteBuffer byteBuffer;
    private CharBuffer charBuffer;
    public InputBuffer(){
        this(DEFAULT_BUFFER_SIZE);
    }
    public InputBuffer(int size){
        byteBuffer = ByteBuffer.allocate(size);
        byteBuffer.rewind().limit(0);
        charBuffer = CharBuffer.allocate(size);
        charBuffer.rewind().limit(0);
    }
    public CoyoteRequest getCoyoteRequest() {
        return coyoteRequest;
    }

    public void setCoyoteRequest(CoyoteRequest coyoteRequest) {
        this.coyoteRequest = coyoteRequest;
    }

    @Override
    public int read(char[] chars, int off, int len) throws IOException {
        return 0;
    }
    public int read(byte[] bytes, int off, int len) throws IOException{
        int rows = coyoteRequest.doRead(byteBuffer);
        int n = Math.min(len, byteBuffer.remaining());
        byteBuffer.get(bytes, off, n);
        return rows;
    }

    @Override
    public void close() throws IOException {

    }
    public void recycle(){

    }
}
