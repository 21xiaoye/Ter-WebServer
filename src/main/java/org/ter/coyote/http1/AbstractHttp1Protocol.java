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
        return null;
    }
}
