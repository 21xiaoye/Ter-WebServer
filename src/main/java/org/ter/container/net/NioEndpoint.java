package org.ter.container.net;

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

    @Override
    public void bind() throws Exception {
        System.out.println("开始创建监听端口......");
        initServerSocket();
    }
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
        startAcceptorThread();
    }

    @Override
    protected void stopInternal() throws Exception {
        if(!running){
            running = true;
            paused = false;
        }
    }
}
