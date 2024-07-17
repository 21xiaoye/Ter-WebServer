package org.ter.container.net;


/**
 * 接收器，用于接受客户端的套接字连接
 * 并将它们传递给工作线程。
 *
 * @param <U> 客户端套接字类型
 */
public class Acceptor<U> implements Runnable{
    private final AbstractEndpoint<?, U> endpoint;
    private String threadName;
    protected volatile ReadyState readyState = ReadyState.NEW;
    private volatile boolean stopCalled = false;
    public Acceptor(AbstractEndpoint<?,U> endpoint) {
        this.endpoint = endpoint;
    }
    public String getThreadName() {
        return threadName;
    }
    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public ReadyState getReadyState() {
        return readyState;
    }

    @Override
    public void run() {
        System.out.println("开始接受连接......");
        while (!stopCalled) {
            while (endpoint.isPaused() && !stopCalled) {
                if (!ReadyState.CLOSING.equals(readyState)) {
                    readyState = ReadyState.CLOSING;
                }
            }
            if (stopCalled) {
                break;
            }
            readyState = ReadyState.OPEN;
            try {
                if (endpoint.isPaused()) {
                    continue;
                }
                U socket;
                try {
                    socket = (U) endpoint.serverSocketAccept();
                    System.out.println("新的连接为"+socket);
                } catch (Exception exception) {
                    if (endpoint.isRunning()) {
                        throw exception;
                    } else {
                        break;
                    }
                }

                if(!stopCalled && !endpoint.isPaused()){
                    if(!endpoint.setSocketOptions(socket)){
                        endpoint.closeSocket(socket);
                    }
                }else{
                    endpoint.destroySocket(socket);
                }
            }catch (Throwable throwable){
                // 记录日志
            }
        }
    }
    public void stop(){
        this.stopCalled = true;
    }





























}
