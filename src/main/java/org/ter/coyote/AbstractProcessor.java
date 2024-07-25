package org.ter.coyote;

import org.ter.util.net.AbstractEndpoint;
import org.ter.util.net.wrapper.SocketWrapperBase;

public abstract class AbstractProcessor extends AbstractProcessorLight{
    protected Adapter adapter;
    protected AbstractEndpoint<?,?> endpoint;
    protected final Request request;
    protected final Response response;
    protected volatile SocketWrapperBase<?> socketWrapper;
    public AbstractProcessor(AbstractEndpoint<?,?> endpoint){
        this(endpoint, new Request(), new Response());
    }
    public AbstractProcessor(AbstractEndpoint<?,?> endpoint, Request request, Response response){
        this.endpoint = endpoint;
        this.request = request;
        this.response = response;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    protected void setSocketWrapper(SocketWrapperBase<?> socketWrapper) {
        this.socketWrapper = socketWrapper;
    }
}
