package org.ter.util.net;

import org.ter.util.net.wrapper.SocketWrapperBase;
import org.ter.ter_server.util.res.StringManager;

import java.net.InetAddress;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

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
    /**
     * socket 属性
     */
    protected final SocketProperties socketProperties = new SocketProperties();
    /**
     * 保存当前所有的连接
     */
    protected Map<U, SocketWrapperBase<S>> connections = new ConcurrentHashMap<>();
    /**
     * 工作线程优先级
     */
    protected int threadPriority = Thread.NORM_PRIORITY;
    private Executor executor = null;
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
    public Executor getExecutor() { return executor; }

    public void setThreadPriority(int threadPriority) {
        this.threadPriority = threadPriority;
    }
    public int getThreadPriority() {
        return threadPriority;
    }
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

    /**
     * 处理给定的套接字包装器，用于触发不同的事件，
     * 将其交给工作线程进行处理
     *
     * @param socketWrapper 要处理的套接字包装器
     * @param socketEvent       要处理的套接字事件
     * @param dispatch          是否交给新容器线程上执行操作
     *
     * @return 已成功处理
     */
    public boolean processSocket(SocketWrapperBase<S> socketWrapper, SocketEvent socketEvent, boolean dispatch){
        if(Objects.isNull(socketWrapper)){
            return false;
        }
        // 创建socket连接处理器
        SocketProcessorBase<S> socketProcessor = createSocketProcessor(socketWrapper, socketEvent);
        Executor executor  = getExecutor();
        if(dispatch && Objects.nonNull(executor)){
            executor.execute(socketProcessor);
        }else{
            socketProcessor.run();
        }
        return true;
    }

    /**
     * 创建线程池
     */
    public void createExecutor(){
        executor = Executors.newFixedThreadPool(200);
    }

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

    /**
     * 初始化EndPoint
     */
    protected abstract void bind() throws Exception;

    /**
     * 解除分配 NIO 内存池，并关闭服务器插槽
     */
    protected abstract void unbind() throws  Exception;

    /**
     * 启动 NIO 端点，创建接受器、轮询器线程。
     */
    protected abstract void startInternal() throws Exception;

    /**
     * 停止EndPoint,停止所有处理线程。
     */
    protected abstract void stopInternal() throws Exception;

    /**
     * 关闭服务套接字通道，停止监听
     */
    protected abstract void doCloseServerSocket() throws Exception;

    /**
     * 获取客户端TCP连接
     * 接受与此通道的套接字建立的连接，接受连接之后
     * 调用 {@link  #setSocketOptions} 对客户端套接字进行封装
     *
     * @return  客户端套接字连接的可选择通道，用于数据的接收和发送
     */
    protected abstract SocketChannel serverSocketAccept() throws Exception;
    /**
     * 包装此套接字连接吗，当客户端建立连接时，服务套接字接受连接，
     * 并调用此方法，将SocketChannel封装
     *
     * @param socket 客户端套接字
     * @return true包装成功
     */
    protected abstract boolean setSocketOptions(U socket);
    /**
     * 创建socket连接处理程序
     *
     * @param socketWrapper socket连接封装器
     * @param socketEvent   当前连接的事件连接状态
     * @return  此socket连接的socket连接处理器
     */
    protected abstract SocketProcessorBase<S> createSocketProcessor(SocketWrapperBase<S> socketWrapper, SocketEvent socketEvent);
}
