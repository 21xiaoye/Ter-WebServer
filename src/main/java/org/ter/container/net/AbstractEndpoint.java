package org.ter.container.net;

import org.ter.ter_server.util.res.StringManager;

import java.net.InetAddress;


/**
 * @param <S> 关联套接字包装器使用的类型
 * @param <U> 所使用的底层套接字的类型
 */
public abstract class AbstractEndpoint <S,U>{
    protected static final StringManager sm = StringManager.getStringManager(AbstractEndpoint.class);
    /**
     * 运行状态
     */
    protected volatile boolean running = false;
    /**
     * 端点暂停时，设置为true
     */
    protected volatile boolean paused = false;
    /**
     * 服务器套接字地址
     */
    private InetAddress address;
    private int port;
    public int getPort() { return port; }
    public void setPort(int port ) { this.port=port; }
    public InetAddress getAddress() { return address; }
    public void setAddress(InetAddress address) { this.address = address; }
    /**
     * 用于接受新连接，并将其交给工作线程处理
     */
    protected Acceptor<U> acceptor;

    public void init() throws Exception{
        System.out.println("底层套接字服务器初始化......");
        bindWithClearUp();
    }
    public void start() throws Exception{
        startInternal();
    }
    public void stop() throws Exception{
        stopInternal();
        unbind();
    }

    protected abstract void bind() throws Exception;
    protected abstract void unbind() throws  Exception;
    protected abstract void startInternal() throws Exception;
    protected abstract void stopInternal() throws Exception;

    private void bindWithClearUp() throws Exception {
        try {
            bind();
        }catch (Throwable throwable){
            unbind();
            throw throwable;
        }
    }


    protected abstract void doCloseServerSocket() throws Exception;


































}
