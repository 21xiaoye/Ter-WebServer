package org.ter.util.net;

import org.ter.util.net.wrapper.NioSocketWrapper;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 轮询器
 */
public class Poller implements Runnable{
    private final Selector selector;
    private volatile boolean close = false;
    public static final int OP_REGISTER = 0x100;
    private final NioEndpoint endpoint;

    private long selectorTimeout = 1000;
    private final BlockingQueue<PollerEvent> events =
            new LinkedBlockingDeque<>();
    public Poller(NioEndpoint endpoint) throws IOException {
        this.endpoint = endpoint;
        this.selector = Selector.open();
    }


    public Selector getSelector() {
        return selector;
    }
    public NioEndpoint getEndpoint() {
        return endpoint;
    }

    @Override
    public void run() {
        while (true){
            try {
                if (!close) {
                    events();
                    selector.select(selectorTimeout);
                }
            } catch (IOException e) {
                // 记录日志
            }
            if(close){
                try {
                    selector.close();
                }catch (IOException exception){
                    // 记录日志
                }
                break;
            }
            Iterator<SelectionKey> iterator =
                    selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                NioSocketWrapper attachment = (NioSocketWrapper)selectionKey.attachment();
                if(Objects.nonNull(attachment)){
                    try {
                        processKey(selectionKey, attachment);
                    } catch (Exception exception) {
                        // 记录日志
                    }
                }
            }

        }
    }

    /**
     * 主要用来处理socket连接的读写
     * @param selectionKey   选择器键
     * @param socketWrapper  socket连接封装器
     * @throws Exception
     */
    protected void processKey(SelectionKey selectionKey, NioSocketWrapper socketWrapper) throws Exception {
        if(selectionKey.isValid()){
            unReg(selectionKey, socketWrapper, selectionKey.readyOps());
            boolean closeSocket = false;
            if(selectionKey.isReadable()){
                if(!endpoint.processSocket(socketWrapper, SocketEvent.OPEN_READ,true)){
                    closeSocket = true;
                }
            }
            if(selectionKey.isWritable()){
                if(!endpoint.processSocket(socketWrapper, SocketEvent.OPEN_WRITE, true)){
                    closeSocket = true;
                }
            }
            if(closeSocket){
                endpoint.cancelledKey(selectionKey, socketWrapper);
            }
        }else {
            endpoint.cancelledKey(selectionKey, socketWrapper);
        }
    }

    /**
     * 将事件放入事件队列当中，等待轮询器进行处理；
     * 当有socket连接时，{@link Acceptor}接收器会接收这个连接，
     * 然后将其放入事件队列当中，等待{@link Poller}轮询器进行处理。
     *
     * @param pollerEvent 轮询事件
     * @throws InterruptedException
     */
    public void addEvent(PollerEvent pollerEvent) throws InterruptedException {
        events.put(pollerEvent);
        selector.wakeup();
    }

    /**
     * 客户端第一次发送请求之后，将其再次放入事件队列当中，等待客户端再一次发送请求
     *
     * @param socketWrapper socket包装器
     * @param interestOps socket在选择器上的注册键操作位
     * @throws InterruptedException
     */
    public void add(NioSocketWrapper socketWrapper, int interestOps) throws InterruptedException {
        PollerEvent pollerEvent = createPollerEvent(socketWrapper, interestOps);
        addEvent(pollerEvent);
        if (close) {
            endpoint.processSocket(socketWrapper, SocketEvent.STOP, false);
        }
    }

    /**
     * 创建轮询事件
     *
     * @param socketWrapper socket包装器
     * @param interestOps socket在选择器上的注册键操作位
     * @return 轮询事件
     */
    private PollerEvent createPollerEvent(NioSocketWrapper socketWrapper, int interestOps) {
        return new PollerEvent(socketWrapper, interestOps);
    }

    /**
     * 向轮询器注册新创建的套接字
     *
     * @param socketWrapper socket包装器
     * @throws InterruptedException
     */
    public void register(final NioSocketWrapper socketWrapper) throws InterruptedException {
        socketWrapper.interestOps(SelectionKey.OP_READ);
        addEvent(createPollerEvent(socketWrapper, OP_REGISTER));
    }

    /**
     * 处理轮询器的事件队列中的事件。
     *
     * @return true表示events中有事件需要处理，并成功处理了事件，false表示没有事件需要处理
     */
    public boolean events(){
        boolean result = false;
        PollerEvent pollerEvent = null;
        for (int i = 0, size = events.size(); i < size  && Objects.nonNull(pollerEvent = events.poll()); i++) {
            result = true;
            NioSocketWrapper socketWrapper = pollerEvent.getSocketWrapper();
            SocketChannel socketChannel = socketWrapper.getSocket().getSocketChannel();
            int interestOps = pollerEvent.getInterestOps();
            if(Objects.isNull(socketChannel)){
                // 关闭连接
            }
            if(OP_REGISTER == interestOps){
                try {
                    socketChannel.register(getSelector(), SelectionKey.OP_READ, socketWrapper);
                } catch (Exception exception) {
                    // 记录日志
                }
            }else{
                System.out.println("这个连接之前已经进行注册了......");
                final SelectionKey key = socketChannel.keyFor(getSelector());
                if (key == null) {
                    socketWrapper.close();
                } else {
                    final NioSocketWrapper attachment = (NioSocketWrapper) key.attachment();
                    if (attachment != null) {
                        // We are registering the key to start with, reset the fairness counter.
                        try {
                            int ops = key.interestOps() | interestOps;
                            attachment.interestOps(ops);
                            key.interestOps(ops);
                        } catch (CancelledKeyException ckx) {
                            endpoint.cancelledKey(key, socketWrapper);
                        }
                    } else {
                        endpoint.cancelledKey(key, socketWrapper);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 防止多个线程重复执行套接字事件。<br/>
     * 例:此时一个Socket被注册到选择器上，为SelectionKey.OP_READ，这时候需要将其交提交给工作线程进行处理，
     * 这时候改变选择键标记，防止多个线程重复执行。
     */
    protected void unReg(SelectionKey sk, NioSocketWrapper socketWrapper, int readyOps) {
        reg(sk, socketWrapper, sk.interestOps() & (~readyOps));
    }

    protected void reg(SelectionKey sk, NioSocketWrapper socketWrapper, int intops) {
        sk.interestOps(intops);
        socketWrapper.interestOps(intops);
    }


    /**
     * 轮询事件
     */
    public static class PollerEvent {
        private NioSocketWrapper socketWrapper;
        private int interestOps;

        public PollerEvent(NioSocketWrapper socketWrapper, int intOps) {
            reset(socketWrapper, intOps);
        }

        public void reset(NioSocketWrapper socketWrapper, int intOps) {
            this.socketWrapper = socketWrapper;
            interestOps = intOps;
        }

        public NioSocketWrapper getSocketWrapper() {
            return socketWrapper;
        }

        public int getInterestOps() {
            return interestOps;
        }

        public void reset() {
            reset(null, 0);
        }

        @Override
        public String toString() {
            return "Poller event: socket [" + socketWrapper.getSocket() + "], socketWrapper [" + socketWrapper +
                    "], interestOps [" + interestOps + "]";
        }
    }
}
