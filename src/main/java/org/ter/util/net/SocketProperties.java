package org.ter.util.net;

public class SocketProperties {
    protected int appReadBufferSize = 8192;
    protected int appWriteBufferSize = 8192;
    /**
     * SO_TIMEOUT option. default is 20000.
     */
    protected Integer soTimeout = Integer.valueOf(20000);

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
    public int getSoTimeout() {
        return soTimeout.intValue();
    }
    public void setSoTimeout(int soTimeout) {
        this.soTimeout = Integer.valueOf(soTimeout);
    }
}
