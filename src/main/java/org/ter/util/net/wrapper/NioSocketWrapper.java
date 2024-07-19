package org.ter.util.net.wrapper;

import org.ter.util.net.NioChannel;
import org.ter.util.net.NioEndpoint;
import org.ter.util.net.Poller;

/**
 * 封装底层Nio套接字
 */
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
//        this.poller = poller;
    }
    public Poller getPoller() {
        return poller;
    }


    public int getInterestOps() {
        return interestOps;
    }

    public int interestOps(int ops) { this.interestOps  = ops; return ops; }
}
