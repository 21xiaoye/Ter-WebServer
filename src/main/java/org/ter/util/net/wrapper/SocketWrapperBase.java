package org.ter.util.net.wrapper;

import org.ter.util.net.AbstractEndpoint;
import org.ter.util.net.SocketBufferHandler;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SocketWrapperBase <E>{
    private E socket;
    private final AbstractEndpoint<E,?> endpoint;
    private final Lock lock = new ReentrantLock();
    protected String localAddr = null;
    private String localName = null;
    private int localPort = -1;
    private String remoteAddr = null;
    private String remoteName = null;
    private int remotePort = -1;
    protected volatile SocketBufferHandler socketBufferHandler = null;
    protected int bufferedWriteSize = 64 * 1024;
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
    public void close() {
    }
}
