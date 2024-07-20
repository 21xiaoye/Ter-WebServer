package org.ter.coyote.http1;

import org.ter.coyote.AbstractProcessor;
import org.ter.util.net.AbstractEndpoint;

public class Http1Processor extends AbstractProcessor {
    public Http1Processor(AbstractEndpoint<?, ?> endpoint) {
        super(endpoint);
    }
}
