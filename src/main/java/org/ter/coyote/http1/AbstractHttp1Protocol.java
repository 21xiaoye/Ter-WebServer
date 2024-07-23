package org.ter.coyote.http1;

import org.ter.coyote.Processor;
import org.ter.util.net.AbstractEndpoint;
import org.ter.coyote.AbstractProtocol;

public abstract class AbstractHttp1Protocol<S> extends AbstractProtocol<S> {
    public AbstractHttp1Protocol(AbstractEndpoint<S, ?> endpoint) {
        super(endpoint);
    }

    @Override
    public Processor createProcessor() {
        Http1Processor http1Processor = new Http1Processor(this, getEndpoint());
        http1Processor.setAdapter(getAdapter());
        return http1Processor;
    }
}
