package org.ter.coyote.http1;

import org.ter.container.net.AbstractEndpoint;
import org.ter.coyote.AbstractProtocol;

public abstract class AbstractHttp1Protocol<S> extends AbstractProtocol<S> {
    public AbstractHttp1Protocol(AbstractEndpoint<S, ?> endpoint) {
        super(endpoint);
    }
}
