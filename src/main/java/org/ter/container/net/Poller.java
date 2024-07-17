package org.ter.container.net;

import org.ter.container.net.wrapper.NioSocketWrapper;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Poller implements Runnable{
    private Selector selector;
    private volatile boolean close = false;
    public static final int OP_REGISTER = 0x100;
    private final ConcurrentLinkedQueue<PollerEvent> events =
            new ConcurrentLinkedQueue<>();
    public Poller() throws IOException {
        this.selector = Selector.open();
    }
    @Override
    public void run() {
        System.out.println("开启轮询器......");
        while (true){
//            boolean hasEvents = false;
//            try {
//                if(!close){
//
//                }
//            }
        }
    }
    public void addEvent(PollerEvent pollerEvent){
        events.add(pollerEvent);
    }
    private PollerEvent createPollerEvent(NioSocketWrapper socketWrapper, int interestOps) {
        return new PollerEvent(socketWrapper, interestOps);
    }
    public void register(final NioSocketWrapper socketWrapper){
        socketWrapper.interestOps(SelectionKey.OP_READ);
        addEvent(createPollerEvent(socketWrapper, OP_REGISTER));
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
