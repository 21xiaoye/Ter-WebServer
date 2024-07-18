package org.ter.container.net;

import org.ter.container.net.wrapper.NioSocketWrapper;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 轮询器
 */
public class Poller implements Runnable{
    private Selector selector;
    private volatile boolean close = false;
    public static final int OP_REGISTER = 0x100;
    private final ConcurrentLinkedQueue<PollerEvent> events =
            new ConcurrentLinkedQueue<>();
    public Poller() throws IOException {
        this.selector = Selector.open();
    }

    public Selector getSelector() {
        return selector;
    }

    @Override
    public void run() {
        System.out.println("开启轮询器......");
        while (true){
            if(close){
                try {
                    selector.close();
                }catch (IOException exception){
                    // 记录日志
                }
                break;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (Objects.isNull(iterator) && iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                NioSocketWrapper attachment = (NioSocketWrapper)selectionKey.attachment();
                if(Objects.nonNull(attachment)){

                }
            }

        }
    }

    protected void processKey(SelectionKey selectionKey, NioSocketWrapper socketWrapper){
        if(selectionKey.isValid()){
            if(selectionKey.isReadable()){

            }
            if(selectionKey.isWritable()){

            }
        }
    }
    public void addEvent(PollerEvent pollerEvent){
        events.add(pollerEvent);
        selector.wakeup();
    }
    private PollerEvent createPollerEvent(NioSocketWrapper socketWrapper, int interestOps) {
        return new PollerEvent(socketWrapper, interestOps);
    }
    public void register(final NioSocketWrapper socketWrapper){
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
            }
        }
        return result;
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
