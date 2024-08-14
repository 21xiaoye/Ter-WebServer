package org.ter.coyote;

import org.ter.util.net.AbstractEndpoint;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.io.IOException;

public abstract class AbstractProcessor extends AbstractProcessorLight implements ActionHook{
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
        this.response.setHook(this);
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

    @Override
    public void action(ActionCode actionCode, Object param) {
        switch (actionCode){
            case COMMIT: {
                if (!response.isCommitted()) {
                    try {
                        prepareResponse();
                    } catch (IOException e) {

                    }
                }
                break;
            }
            case CLOSE, ACK: {
               break;
            }
        }
    }

    /**
     * 准备响应数据
     * @throws IOException 在此过程当中发生I/O错误
     */
    protected abstract void prepareResponse() throws IOException;
}
