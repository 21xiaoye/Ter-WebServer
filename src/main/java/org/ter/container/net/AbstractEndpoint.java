package org.ter.container.net;

import org.ter.ter_server.util.res.StringManager;

import java.net.InetAddress;
import java.nio.channels.SocketChannel;


/**
 * @param <S> 关联的套接字包装器使用的类型
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
    private String name = "TP";
    /**
     * 默认值为 true - 创建的线程将处于守护进程模式。如果设置为 false，则控制线程将不是守护进程 - 并且将使进程保持活动状态。
     */
    private boolean daemon = true;
    /**
     * 服务套接字监听的端口
     */
    private int port;
    /**
     * 用于接受新连接，并将其交给工作线程处理
     */
    protected Acceptor<U> acceptor;
    public int getPort() { return port; }
    public void setPort(int port ) { this.port = port; }
    public InetAddress getAddress() { return address; }
    public void setAddress(InetAddress address) { this.address = address; }

    public boolean isPaused() {
        return paused;
    }
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean getDaemon(){
        return daemon;
    }
    public void setDaemon(boolean daemon){
        this.daemon = daemon;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

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
    /**
     * 开启接受线程，用于接收网络连接
     */
    protected void startAcceptorThread(){
        acceptor  = new Acceptor<>(this);
        String threadName = getName() + "-Acceptor";
        acceptor.setThreadName(threadName);
        Thread thread = new Thread(acceptor, threadName);
        thread.setDaemon(daemon);
        thread.start();
    }
    protected abstract void doCloseServerSocket() throws Exception;
    protected abstract SocketChannel serverSocketAccept() throws Exception;
    protected abstract boolean setSocketOptions(U socket);

    /**
     * 在配置接受的套接字或尝试分派其进行处理时发生错误时，
     * 必须立即关闭连接时调用。
     * 与套接字关联的包装器将用于关闭
     *
     * @param socket 新接受的客户端套接字连接
     */
    protected void closeSocket(U socket){
        // 关闭套接字
    }

    /**
     * 销毁关闭客户端套接字连接
     * 当连接器未处于允许处理套接字的状态，
     * 或者存在阻止分配套接字包装器的错误时，将使用此方法。
     *
     * @param socket 新接受的套接字连接
     */
    protected abstract void destroySocket(U socket);
}
