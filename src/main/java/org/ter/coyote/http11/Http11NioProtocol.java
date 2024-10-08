package org.ter.coyote.http11;

import org.ter.util.net.NioChannel;
import org.ter.util.net.NioEndpoint;
import org.ter.coyote.Adapter;
import java.util.concurrent.Executor;

public class Http11NioProtocol extends AbstractHttp11Protocol<NioChannel> {
    public Http11NioProtocol() {
        this(new NioEndpoint());
    }
    public Http11NioProtocol(NioEndpoint endpoint) {
        super(endpoint);
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
