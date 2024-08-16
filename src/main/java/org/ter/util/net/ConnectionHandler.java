package org.ter.util.net;

import org.ter.coyote.AbstractProtocol;
import org.ter.coyote.Processor;
import org.ter.coyote.SocketState;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.util.Objects;

/**
 * 套接字连接的具体处理程序
 */
public record ConnectionHandler<S>(AbstractProtocol<S> protocol) implements Handler<S> {
    @Override
    public SocketState process(SocketWrapperBase<S> socketWrapper, SocketEvent status) {
        if (Objects.isNull(socketWrapper) || SocketEvent.DISCONNECT.equals(status) || SocketEvent.ERROR.equals(status)) {
            return SocketState.CLOSED;
        }
        S socket = socketWrapper.getSocket();
        Processor processor = (Processor) socketWrapper.takeCurrentProcessor();
        if (Objects.nonNull(processor)) {

        } else {
            processor = protocol().createProcessor();
        }
        try {
            SocketState state = processor.process(socketWrapper, status);
            switch (state){
                case LONG -> {
                    System.out.println(SocketState.LONG);
                }
                case OPEN -> {
                    System.out.println(SocketState.OPEN);
                }
                case SENDFILE -> {
                    System.out.println(SocketState.SENDFILE);
                }
                case UPGRADED -> {
                    System.out.println(SocketState.UPGRADED);
                }
                case SUSPENDED -> {
                    System.out.println(SocketState.SUSPENDED);
                }
                default -> {
                    processor.recycle();
                    System.out.println("其它");
                }
            }
        }catch (Exception exception){
            // 记录日志
        }
        return null;
    }
}
