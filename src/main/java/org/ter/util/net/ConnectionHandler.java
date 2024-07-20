package org.ter.util.net;

import org.ter.coyote.AbstractProtocol;
import org.ter.coyote.SocketState;
import org.ter.util.net.wrapper.SocketWrapperBase;

/**
 * 套接字连接的具体处理程序
 */
public class ConnectionHandler<S> implements Handler<S>{
    private final AbstractProtocol<S> protocol;
    public ConnectionHandler(AbstractProtocol<S> protocol){
        this.protocol = protocol;
    }
    @Override
    public SocketState process(SocketWrapperBase<S> socket, SocketEvent status) {
        System.out.println("处理连接"+status.toString());
        return null;
    }
}
