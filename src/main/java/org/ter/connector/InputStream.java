package org.ter.connector;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;

public class InputStream extends ServletInputStream {
    private InputBuffer inputBuffer;
    public InputStream(InputBuffer inputBuffer){
        this.inputBuffer = inputBuffer;
    }
    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }
    @Override
    public void setReadListener(ReadListener readListener) {

    }
    @Override
    public int read() throws IOException {
        return inputBuffer.read();
    }
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return inputBuffer.read(b, off, len);
    }
}
