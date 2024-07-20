package org.ter.coyote;

import org.ter.util.net.SocketEvent;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.io.IOException;

public abstract class AbstractProcessorLight implements Processor{
    @Override
    public SocketState process(SocketWrapperBase<?> socketWrapper, SocketEvent socketEvent) throws IOException {
        return null;
    }
}
