package org.ter.container.net;

import java.io.IOException;
import java.nio.channels.Selector;

/**
 * 轮询器
 */
public class Poller implements Runnable{
    private Selector selector;
    private volatile boolean close = false;
    public Poller() throws IOException{
        this.selector = Selector.open();
    }
    @Override
    public void run() {
        while (true){
//            boolean hasEvents = false;
//            try {
//                if(!close){
//
//                }
//            }
        }
    }

    /**
     * 轮询事件
     */
    public static class PollerEvent{

    }

























}
