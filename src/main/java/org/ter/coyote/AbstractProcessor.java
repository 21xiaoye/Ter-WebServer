package org.ter.coyote;

import org.ter.util.net.AbstractEndpoint;
import org.ter.util.net.wrapper.SocketWrapperBase;

public abstract class AbstractProcessor extends AbstractProcessorLight{
    protected Adapter adapter;
    protected AbstractEndpoint<?,?> endpoint;
    protected final CoyoteRequest request;
    protected final CoyoteResponse response;
    protected volatile SocketWrapperBase<?> socketWrapper;
    public AbstractProcessor(AbstractEndpoint<?,?> endpoint){
        this(endpoint, new CoyoteRequest(), new CoyoteResponse());
    }
    public AbstractProcessor(AbstractEndpoint<?,?> endpoint, CoyoteRequest request, CoyoteResponse response){
        this.endpoint = endpoint;
        this.request = request;
        this.response = response;
        this.request.setResponse(response);
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public CoyoteRequest getRequest() {
        return request;
    }

    protected void setSocketWrapper(SocketWrapperBase<?> socketWrapper) {
        this.socketWrapper = socketWrapper;
    }
}
