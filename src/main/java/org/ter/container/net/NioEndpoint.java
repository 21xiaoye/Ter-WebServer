package org.ter.container.net;

import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * 处理网络连接
 */
public class NioEndpoint extends AbstractEndpoint<ByteChannel, SocketChannel> {
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
        InetSocketAddress socketAddress = new InetSocketAddress(getAddress(), getPort());
        serverSocket.socket().bind(socketAddress);
        serverSocket.configureBlocking(true);
        System.out.println("服务器监听"+getAddress()+":"+getPort()+"成功");
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
    }

    @Override
    protected void stopInternal() throws Exception {
        if(!running){
            running = true;
            paused = false;
        }
    }
}
