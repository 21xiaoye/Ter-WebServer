package org.ter.connector;

import org.ter.coyote.CoyoteRequest;
import org.ter.coyote.CoyoteResponse;

import java.io.IOException;
import java.io.Reader;

public class InputBuffer extends Reader {
    private CoyoteRequest coyoteRequest;

    public CoyoteRequest getCoyoteRequest() {
        return coyoteRequest;
    }

    public void setCoyoteRequest(CoyoteRequest coyoteRequest) {
        this.coyoteRequest = coyoteRequest;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
    public void recycle(){

    }
}
