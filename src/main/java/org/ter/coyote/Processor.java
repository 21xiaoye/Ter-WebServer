package org.ter.coyote;

import org.ter.util.net.SocketEvent;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.io.IOException;

/**
 * 协议处理器通用接口，是连接器三大组件之一，与{@link org.ter.util.net.AbstractEndpoint}
 * 抽象成{@link ProtocolHandler}
 * 主要作用是接受EndPoint组件的Socket包装器，对其进行处理，
 * 并转为{@link CoyoteRequest} 对象，交给适配器{@link Adapter}进行处理
 */
public interface Processor {
    /**
     * 处理连接。每当发生事件（例如，更多数据到达）时，都会调用该事件，该事件允许继续处理当前未被处理的连接
     *
     * @param socketWrapper 连接
     * @param socketEvent 触发额外处理程序时连接状态
     * @return 返回套接字连接状态
     * @throws java.io.IOException 处理请求期间发生I/O错误
     */
    SocketState process(SocketWrapperBase<?> socketWrapper, SocketEvent socketEvent) throws IOException;

    /**
     * 与此处理器相关的请求
     *
     * @return 返回与此处理器相关的请求
     */
    CoyoteRequest getRequest();
    void recycle();
}
