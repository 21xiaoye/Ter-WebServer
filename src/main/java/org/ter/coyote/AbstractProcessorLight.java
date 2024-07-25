package org.ter.coyote;

import org.ter.util.net.SocketEvent;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.io.IOException;

public abstract class AbstractProcessorLight implements Processor{
    @Override
    public SocketState process(SocketWrapperBase<?> socketWrapper, SocketEvent socketEvent) throws IOException {
        SocketState state = SocketState.CLOSED;
        do{
            if(SocketEvent.OPEN_READ.equals(socketEvent)){
                service(socketWrapper);
            }else if(SocketEvent.OPEN_WRITE.equals(socketEvent)){
                state = SocketState.LONG;
            }else{
                state = SocketState.CLOSED;
            }
        }while (SocketState.CLOSED.equals(state));
        return state;
    }

    /**
     * 处理标准的 HTTP 请求，完全读取标头后，在有新的 HTTP 请求要处理之前，将不会再次调用此方法。
     * 在处理过程中，请求类型可能会更改，这可能会导致对 dispatch(SocketEvent)的一个或多个调用，
     *
     * @param socketWrapper 此处理过程的连接
     * @return  处理完成之后的请求连接状态
     * @throws IOException 在处理请求的过程当中发生I/O错误
     */
    protected abstract SocketState service(SocketWrapperBase<?> socketWrapper) throws IOException;
}
