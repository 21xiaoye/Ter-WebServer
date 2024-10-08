package org.ter.util.net;

import org.ter.coyote.AbstractProtocol;
import org.ter.coyote.Processor;
import org.ter.coyote.SocketState;
import org.ter.util.net.wrapper.SocketWrapperBase;

import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.WebConnection;
import java.nio.ByteBuffer;
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
        Processor processor = (Processor) socketWrapper.takeCurrentProcessor();
        if (Objects.nonNull(processor)) {
        } else {
            processor = protocol().createProcessor();
        }
        SocketState state = SocketState.CLOSED;
        try {
            state = processor.process(socketWrapper, status);
            processor.recycle();
            switch (state){
                case LONG -> {
                    System.out.println(SocketState.LONG);
                }
                case OPEN -> {
                    socketWrapper.registerReadInterest();
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
                    System.out.println("其它");
                }
            }
        }catch (Exception exception){
            // 记录日志
        }
        if(Objects.nonNull(processor)){
            socketWrapper.setCurrentProcessor(processor);
        }
        return state;
    }

    @Override
    public void release(SocketWrapperBase<S> socketWrapper) {
        Processor processor = (Processor)socketWrapper.takeCurrentProcessor();
        processor.recycle();
    }
}
