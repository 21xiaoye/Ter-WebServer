package org.ter.util.net.wrapper;

import org.ter.util.net.NioChannel;
import org.ter.util.net.NioEndpoint;
import org.ter.util.net.Poller;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class NioSocketWrapper extends SocketWrapperBase<NioChannel> {
    private final Poller poller;
    private int interestOps = 0;
    private final Object readLock;
    private volatile boolean readBlocking = false;
    private final Object writeLock ;
    private volatile boolean writeBlocking = false;
    public NioSocketWrapper(NioChannel channel, NioEndpoint endpoint){
        super(channel, endpoint);
        poller = endpoint.getPoller();
        readLock = new Object();
        writeLock = new Object();
        socketBufferHandler = channel.getBufferHandler();
    }
    public Poller getPoller() {
        return poller;
    }


    public int getInterestOps() {
        return interestOps;
    }

    public int interestOps(int ops) { this.interestOps  = ops; return ops; }

    @Override
    public int read(ByteBuffer to) throws IOException {
        return getSocket().read(to);
    }
    @Override
    public int write(ByteBuffer to) throws IOException {
        return getSocket().write(to);
    }

    @Override
    public void populateLocalAddr() {
        SocketChannel socketChannel = getSocket().getSocketChannel();
        if(Objects.nonNull(socketChannel)){
            InetAddress localAddress = socketChannel.socket().getLocalAddress();
            if(Objects.nonNull(localAddress)){
                localAddr = localAddress.getHostAddress();
            }
        }
    }

    @Override
    public void populateLocalHost() {
        SocketChannel socketChannel = getSocket().getSocketChannel();
        if(Objects.nonNull(socketChannel)){
            InetAddress localAddress = socketChannel.socket().getLocalAddress();
            if(Objects.nonNull(localAddress)){
                localName = localAddress.getHostName();
            }
        }
    }

    @Override
    public void populateLocalPort() {
        SocketChannel socketChannel = getSocket().getSocketChannel();
        if(Objects.nonNull(socketChannel)){
            localPort = socketChannel.socket().getLocalPort();
        }
    }

    @Override
    public void populateRemotePort() {
        SocketChannel socketChannel = getSocket().getSocketChannel();
        if(Objects.nonNull(socketChannel)){
            localPort = socketChannel.socket().getPort();
        }
    }

    @Override
    public void populateRemoteHost() {
        SocketChannel socketChannel = getSocket().getSocketChannel();
        if(Objects.nonNull(socketChannel)){
            InetAddress localAddress = socketChannel.socket().getInetAddress();
            if(Objects.nonNull(localAddress)){
                remoteName = localAddress.getHostName();
            }
        }
    }

    @Override
    public void populateRemoteAddr() {
        SocketChannel socketChannel = getSocket().getSocketChannel();
        if(Objects.nonNull(socketChannel)){
            InetAddress localAddress = socketChannel.socket().getInetAddress();
            if(Objects.nonNull(localAddress)){
                remoteAddr = localAddress.getHostAddress();
            }
        }
    }
    @Override
    public void registerReadInterest() throws InterruptedException {
        getPoller().add(this, SelectionKey.OP_READ);
    }
}
