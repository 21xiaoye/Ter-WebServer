package org.ter.util.net;

import org.ter.coyote.SocketState;
import org.ter.util.net.wrapper.SocketWrapperBase;

/**
 * 处理Socket连接的处理程序接口
 * @param <S>
 */
public interface Handler<S> {
    /**
     * 根据给定的套接字状态，处理当前连接状态的socket套接字
     * @param socket 要处理的socket套接字（封装）
     * @param status 要处理的socket套接字的状态
     * @return 处理后的套接字状态
     */
    SocketState process(SocketWrapperBase<S> socket,
                        SocketEvent status);
}
