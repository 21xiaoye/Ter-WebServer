package org.ter.connector;

import org.ter.coyote.CoyoteResponse;

import java.io.IOException;
import java.io.Writer;

public class OutBuffer extends Writer {
    private CoyoteResponse coyoteResponse;

    public CoyoteResponse getCoyoteResponse() {
        return coyoteResponse;
    }

    public void setCoyoteResponse(CoyoteResponse coyoteResponse) {
        this.coyoteResponse = coyoteResponse;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {

    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public void close() throws IOException {

    }
}
