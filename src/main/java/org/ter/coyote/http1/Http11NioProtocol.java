package org.ter.coyote.http1;

import org.ter.container.net.AbstractEndpoint;
import org.ter.container.net.NioChannel;
import org.ter.container.net.NioEndpoint;
import org.ter.coyote.Adapter;

import java.nio.channels.ByteChannel;
import java.util.concurrent.Executor;

public class Http11NioProtocol extends AbstractHttp1Protocol<NioChannel> {
    public Http11NioProtocol() {
        this(new NioEndpoint());
    }
    public Http11NioProtocol(AbstractEndpoint<NioChannel, ?> endpoint) {
        super(endpoint);
    }


    @Override
    public Adapter getAdapter() {
        return null;
    }

    @Override
    public void setAdapter(Adapter adapter) {

    }

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public void pause() throws Exception {

    }

    @Override
    public void resume() throws Exception {

    }

    @Override
    public void stop() throws Exception {

    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void closeServerSocket() {

    }

    @Override
    public long awaitConnectionsClose(long waitMillis) {
        return 0;
    }
}
