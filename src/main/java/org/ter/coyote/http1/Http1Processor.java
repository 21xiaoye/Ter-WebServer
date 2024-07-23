package org.ter.coyote.http1;

import org.ter.coyote.AbstractProcessor;
import org.ter.coyote.SocketState;
import org.ter.util.net.AbstractEndpoint;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.io.IOException;

public class Http1Processor extends AbstractProcessor {
    private final AbstractHttp1Protocol<?> protocol;
    protected Http1InputBuffer inputBuffer;
    private Http1OutputBuffer outputBuffer;
    /**
     * 这个用来是否是长连接的标识，用来管理HTTP请求持久化。
     * true表示完成一次请求之后，不会中断连接，而是继续等待后续请求并处理。
     */
    protected volatile boolean keepAlive = true;
    public Http1Processor(AbstractHttp1Protocol<?> protocol,AbstractEndpoint<?, ?> endpoint) {
        super(endpoint);
        this.protocol = protocol;
    }

    @Override
    protected SocketState service(SocketWrapperBase<?> socketWrapper) throws IOException {
        setSocketWrapper(socketWrapper);
        while (!endpoint.isPaused()){

        }
        return null;
    }

    @Override
    protected void setSocketWrapper(SocketWrapperBase<?> socketWrapper) {
        super.setSocketWrapper(socketWrapper);
    }
}