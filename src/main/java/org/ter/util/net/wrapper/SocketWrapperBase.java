package org.ter.util.net.wrapper;

import org.ter.util.net.AbstractEndpoint;
import org.ter.util.net.SocketBufferHandler;
import org.ter.util.net.SocketEvent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Socket封装基类
 * @param <E> 底层Socket通道类型
 */
public abstract class SocketWrapperBase <E>{
    private E socket;
    private final AbstractEndpoint<E,?> endpoint;
    private final Lock lock = new ReentrantLock();
    protected String localAddr = null;
    protected String localName = null;
    protected int localPort = -1;
    protected String remoteAddr = null;
    protected String remoteName = null;
    protected int remotePort = -1;
    protected volatile SocketBufferHandler socketBufferHandler = null;
    protected int bufferedWriteSize = 64 * 1024;
    /**
     * 此Socket包装器相关联的Processor实例，
     * 只有在{@link org.ter.util.net.Handler#process(SocketWrapperBase, SocketEvent)}
     * 中对其进行填充Processor实例
     */
    private final AtomicReference<Object> currentProcessor = new AtomicReference<>();
    public SocketWrapperBase(E socket, AbstractEndpoint<E, ?> endpoint){
        this.endpoint = endpoint;
        this.socket = socket;
    }
    public Lock getLock(){
        return lock;
    }
    public E getSocket() {
        return socket;
    }
    public AbstractEndpoint<E, ?> getEndpoint() {
        return endpoint;
    }
    public Object takeCurrentProcessor(){
        return currentProcessor.getAndSet(null);
    }

    public SocketBufferHandler getSocketBufferHandler() {
        return socketBufferHandler;
    }

    public void setSocketBufferHandler(SocketBufferHandler socketBufferHandler) {
        this.socketBufferHandler = socketBufferHandler;
    }

    public void close() {
    }

    /**
     * 从SocketChannel中读取数据到缓冲区当中去
     * @param to 需要读入的缓冲区
     * @return 读取的字节数
     * @throws IOException  在读取的过程当中发生I/O错误
     */
    public abstract int read(ByteBuffer to) throws IOException;
    public abstract int write(ByteBuffer to) throws IOException;

    /**
     * 填充本地服务的服务地址
     */
    public abstract void populateLocalAddr();

    /**
     * 填充本地服务的服务端口
     */
    public abstract void populateLocalPort();
    /**
     * 填充本地服务的服务主机名
     */
    public abstract void populateLocalHost();
    /**
     * 填充远程连接的地址
     */
    public abstract void populateRemotePort();

    /**
     * 填充远程连接的主机名
     */
    public abstract void populateRemoteHost();

    /**
     * 填充远程连接的端口
     */
    public abstract void populateRemoteAddr();

    public int getLocalPort() {
        if(localPort ==  -1){
            populateLocalPort();
        }
        return localPort;
    }

    public int getRemotePort() {
        if(remotePort == -1){
            populateRemotePort();
        }
        return remotePort;
    }

    public String getLocalAddr() {
        if(Objects.isNull(localAddr)){
            populateLocalAddr();
        }
        return localAddr;
    }

    public String getRemoteAddr() {
        if(Objects.isNull(remoteAddr)){
            populateRemoteAddr();
        }
        return remoteAddr;
    }

    public String getLocalName() {
        if(Objects.isNull(localName)){
            populateLocalHost();
        }
        return localName;
    }

    public String getRemoteName() {
        if(Objects.isNull(remoteName)){
            populateRemoteHost();
        }
        return remoteName;
    }
}
