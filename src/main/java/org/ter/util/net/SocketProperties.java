package org.ter.util.net;

public class SocketProperties {
    protected int appReadBufferSize = 8192;
    protected int appWriteBufferSize = 8192;

    public int getAppReadBufferSize() {
        return appReadBufferSize;
    }

    public void setAppReadBufferSize(int appReadBufferSize) {
        this.appReadBufferSize = appReadBufferSize;
    }

    public int getAppWriteBufferSize() {
        return appWriteBufferSize;
    }

    public void setAppWriteBufferSize(int appWriteBufferSize) {
        this.appWriteBufferSize = appWriteBufferSize;
    }
}
