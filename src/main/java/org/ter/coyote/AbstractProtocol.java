package org.ter.coyote;

import org.ter.container.net.AbstractEndpoint;
import org.ter.ter_server.util.res.StringManager;

public abstract class AbstractProtocol<S> implements ProtocolHandler{
    private static final StringManager sm = StringManager.getStringManager(AbstractProtocol.class);
    /**
     * 底层网络的I/O端点
     */
    private final AbstractEndpoint<S,?> endpoint;
    public AbstractProtocol(AbstractEndpoint<S, ?> endpoint){
        this.endpoint = endpoint;
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
}























