package org.ter.util.net;

import org.ter.util.net.wrapper.SocketWrapperBase;

import java.util.concurrent.locks.Lock;

/**
 * Socket连接处理基类
 * @param <S> 关联的套接字包装器使用的类型
 */
public abstract class SocketProcessorBase<S> implements Runnable{
    protected SocketWrapperBase<S> socketWrapper;
    protected SocketEvent socketEvent;
    public SocketProcessorBase(SocketWrapperBase<S> socketWrapper, SocketEvent socketEvent){
        this.socketWrapper = socketWrapper;
        this.socketEvent = socketEvent;
    }

    @Override
    public void run() {
        Lock lock = socketWrapper.getLock();
        lock.lock();
        try {
            doRun();
        }finally {
            lock.unlock();
        }
    }
    protected abstract void doRun();
}
