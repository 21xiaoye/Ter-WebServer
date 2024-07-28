package org.ter.coyote;

import org.ter.util.net.AbstractEndpoint;
import org.ter.util.res.StringManager;
import org.ter.util.net.ConnectionHandler;
import org.ter.util.net.Handler;

public abstract class AbstractProtocol<S> implements ProtocolHandler{
    private static final StringManager sm = StringManager.getStringManager(AbstractProtocol.class);
    /**
     * 底层网络的I/O端点
     */
    private final AbstractEndpoint<S,?> endpoint;
    private Handler<S> handler;
    protected Adapter adapter;
    public AbstractProtocol(AbstractEndpoint<S, ?> endpoint){
        this.endpoint = endpoint;
        ConnectionHandler<S> cHandler = new ConnectionHandler<>(this);
        setHandler(cHandler);
        getEndpoint().setHandler(cHandler);
    }

    protected AbstractEndpoint<S,?> getEndpoint(){
        return endpoint;
    }
    public int getPort() {
        return endpoint.getPort();
    }

    public void setPort(int port) {
        endpoint.setPort(port);
    }

    public Handler<S> getHandler() {
        return handler;
    }

    public void setHandler(Handler<S> handler) {
        this.handler = handler;
    }

    @Override
    public Adapter getAdapter() {
        return adapter;
    }
    @Override
    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void init() throws Exception {
        System.out.println("开始初始化协议处理器......");
        endpoint.init();
    }

    @Override
    public void start() throws Exception {
        System.out.println("开始启动协议处理器......");
        endpoint.start();
    }

    /**
     * 为当前协议配置新的Processor实例
     * @return 新的Processor实例
     */
    public abstract Processor createProcessor();
}























