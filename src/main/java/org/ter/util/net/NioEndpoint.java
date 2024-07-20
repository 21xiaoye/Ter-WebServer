package org.ter.util.net;

import org.ter.coyote.SocketState;
import org.ter.util.net.wrapper.NioSocketWrapper;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * 处理网络连接
 */
public class NioEndpoint extends AbstractEndpoint<NioChannel, SocketChannel> {
    /**
     * 服务套接字
     */
    private volatile ServerSocketChannel serverSocket = null;
    /**
     * 等待轮询器结束
     */
    private volatile CountDownLatch stopLatch = null;

    /**
     * 套接字轮询器
     */
    private Poller poller = null;

    public Poller getPoller() {
        return poller;
    }
    @Override
    public void bind() throws Exception {
        System.out.println("开始创建监听端口......");
        initServerSocket();
    }

    /**
     * 初始化服务套接字
     */
    protected void initServerSocket() throws Exception{
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(getPort()));
        serverSocket.configureBlocking(true);
        System.out.println("服务器监听端口"+getPort()+"成功");
    }

    @Override
    protected void doCloseServerSocket() throws Exception{
        if(Objects.nonNull(serverSocket)){
            serverSocket.close();
        }
        serverSocket = null;
    }

    @Override
    protected void unbind() throws Exception {
        if(running){
            stop();
        }
        try {
            doCloseServerSocket();
        }catch (Exception exception){
            // 记录日志
        }
    }

    @Override
    protected void startInternal() throws Exception {
        System.out.println("启动成功......");
        if(Objects.isNull(getExecutor())){
            createExecutor();
        }
        startAcceptorThread();
        startPollerThread();
    }

    /**
     * 开启轮询器线程
     */
    private void startPollerThread() throws Exception{
        poller = new Poller(this);
        Thread pollerThread = new Thread(poller, getName() + "-Poller");
        pollerThread.setPriority(threadPriority);
        pollerThread.setDaemon(true);
        pollerThread.start();
    }
    @Override
    protected void stopInternal() throws Exception {
        if(!running){
            running = true;
            paused = false;
        }
    }

    @Override
    protected SocketChannel serverSocketAccept() throws Exception {
        SocketChannel channel = serverSocket.accept();
        if(Objects.nonNull(channel)){
            System.out.println("有新的连接了");
        }
        return channel;
    }

    @Override
    protected boolean setSocketOptions(SocketChannel socket) {
        NioSocketWrapper newWrapper = null;
        try {
            SocketBufferHandler socketBufferHandler = new SocketBufferHandler(socketProperties.getAppReadBufferSize(), socketProperties.getAppWriteBufferSize());
            NioChannel channel = new NioChannel(socketBufferHandler);
            newWrapper = new NioSocketWrapper(channel, this);
            channel.reset(socket,newWrapper);
            connections.put(socket, newWrapper);
            socket.configureBlocking(false);
            poller.register(newWrapper);
            return true;
        }catch (Throwable throwable){
            System.out.println("endpoint.socketOptionsError"+throwable);
            if(Objects.nonNull(newWrapper)){
                destroySocket(socket);
            }
        }
        return false;
    }

    @Override
    protected SocketProcessorBase<NioChannel> createSocketProcessor(SocketWrapperBase<NioChannel> socketWrapper, SocketEvent socketEvent) {
        return new NioSocketProcessor(socketWrapper, socketEvent);
    }

    @Override
    protected void destroySocket(SocketChannel socket) {

    }
    public void cancelledKey(SelectionKey sk, SocketWrapperBase<NioChannel> socketWrapper) {
        try {
            if (sk != null) {
                sk.attach(null);
                if (sk.isValid()) {
                    sk.cancel();
                }
            }
        }finally {
            socketWrapper.close();
        }
    }
    /**
     * socket连接处理基类的具体实现类
     * 相当于工作线程
     */
    public class NioSocketProcessor extends SocketProcessorBase<NioChannel>{
        public NioSocketProcessor(SocketWrapperBase<NioChannel> socketWrapper, SocketEvent socketEvent) {
            super(socketWrapper, socketEvent);
        }

        @Override
        protected void doRun() {
            System.out.println("收到socket连接。。。。。。");
            Poller poller = NioEndpoint.this.poller;
            if(Objects.isNull(poller)){
                socketWrapper.close();
                return;
            }
            SocketState socketState = SocketState.OPEN;
            if(Objects.isNull(socketEvent)){
                getHandler().process(socketWrapper, SocketEvent.OPEN_READ);
            }else{
                getHandler().process(socketWrapper, socketEvent);
            }
            if(SocketState.CLOSED.equals(socketState)){
                poller.getEndpoint().cancelledKey(getSelectionKey(), socketWrapper);
            }
        }
        private SelectionKey getSelectionKey() {
            SocketChannel socketChannel = socketWrapper.getSocket().getSocketChannel();
            if (socketChannel == null) {
                return null;
            }

            return socketChannel.keyFor(NioEndpoint.this.poller.getSelector());
        }
    }
}
