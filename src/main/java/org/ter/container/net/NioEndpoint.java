package org.ter.container.net;

import org.ter.container.net.wrapper.NioSocketWrapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
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
     *
     * @throws Exception 初始化服务套接字时发生错误
     */
    protected void initServerSocket() throws Exception{
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(getPort()));
        serverSocket.configureBlocking(true);
        System.out.println("服务器监听端口"+getPort()+"成功");
    }

    /**
     * 关闭服务套接字
     *
     * @throws Exception 关闭过程发生错误
     */
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
        startPollerThread();
        startAcceptorThread();
    }

    /**
     * 开启轮询器线程
     *
     * @throws Exception 开启过程发生错误
     */
    private void startPollerThread() throws Exception{
        poller = new Poller();
        Thread pollerThread = new Thread(poller, getName() + "-Poller");
        pollerThread.setPriority(threadPriority);
        pollerThread.setDaemon(true);
        pollerThread.start();
    }

    /**
     * 关闭服务套机字
     *
     * @throws Exception 关闭过程发生错误
     */
    @Override
    protected void stopInternal() throws Exception {
        if(!running){
            running = true;
            paused = false;
        }
    }

    /**
     * 获取客户端TCP连接
     * 接受与此通道的套接字建立的连接
     *
     * @return  客户端套接字连接
     * @throws Exception 接受连接过程发生错误
     */
    @Override
    protected SocketChannel serverSocketAccept() throws Exception {
        SocketChannel channel = serverSocket.accept();
        if(Objects.nonNull(channel)){
            System.out.println("有新的连接了");
        }
        return channel;
    }

    /**
     * 包装此套接字连接
     *
     * @param socket 套接字连接
     * @return true包装成功
     */
    @Override
    protected boolean setSocketOptions(SocketChannel socket) {
        NioSocketWrapper socketWrapper = null;
        try {

            NioChannel channel = null;

            SocketBufferHandler socketBufferHandler = new SocketBufferHandler(socketProperties.getAppReadBufferSize(), socketProperties.getAppWriteBufferSize());
            channel = new NioChannel(socketBufferHandler);
            NioSocketWrapper newWrapper = new NioSocketWrapper(channel, this);
            channel.reset(socket,newWrapper);
            connections.put(socket, newWrapper);
            socketWrapper = newWrapper;

            socket.configureBlocking(false);
            poller.register(socketWrapper);
            return true;
        }catch (Throwable throwable){
            System.out.println("endpoint.socketOptionsError"+throwable);
            if(Objects.nonNull(socketWrapper)){
                destroySocket(socket);
            }
        }
        return false;
    }

    @Override
    protected void destroySocket(SocketChannel socket) {

    }
}
