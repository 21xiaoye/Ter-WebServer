package org.ter.container.net;

public class Acceptor <U> implements Runnable{
    private final AbstractEndpoint<?, U> endpoint;
    private String threadName;
    public Acceptor(AbstractEndpoint<?,U> endpoint) {
        this.endpoint = endpoint;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void run() {
        System.out.println("开启接收连接......");
    }
}
