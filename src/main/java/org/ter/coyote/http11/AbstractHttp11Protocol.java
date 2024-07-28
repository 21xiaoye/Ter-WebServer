package org.ter.coyote.http11;

import org.ter.coyote.Processor;
import org.ter.util.net.AbstractEndpoint;
import org.ter.coyote.AbstractProtocol;

public abstract class AbstractHttp11Protocol<S> extends AbstractProtocol<S> {
    private int maxHttpResponseHeaderSize = -1;

    public int getMaxHttpResponseHeaderSize() {
        return maxHttpResponseHeaderSize == -1 ? getMaxHttpHeaderSize() : maxHttpResponseHeaderSize;
    }

    public void setMaxHttpResponseHeaderSize(int maxHttpResponseHeaderSize) {
        maxHttpResponseHeaderSize = maxHttpResponseHeaderSize;
    }
    private int maxHttpHeaderSize = 8 * 1024;

    public int getMaxHttpHeaderSize() {
        return maxHttpHeaderSize;
    }
    public void setMaxHttpHeaderSize(int maxHttpHeaderSize) {
        this.maxHttpHeaderSize = maxHttpHeaderSize;
    }

    public AbstractHttp11Protocol(AbstractEndpoint<S, ?> endpoint) {
        super(endpoint);
    }

    @Override
    public Processor createProcessor() {
        Http11Processor http11Processor = new Http11Processor(this, getEndpoint());
        http11Processor.setAdapter(getAdapter());
        return http11Processor;
    }
}
