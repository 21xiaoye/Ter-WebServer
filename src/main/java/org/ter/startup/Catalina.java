package org.ter.startup;

import org.ter.container.Server;
import org.ter.util.res.StringManager;

public class Catalina {
    private static final StringManager sm = StringManager.getStringManager(Catalina.class);
    protected boolean await = false;
    protected ClassLoader parentClassLoader = Catalina.class.getClassLoader();
    private Server server = null;
    protected boolean useShutdownHook = true;
    protected Thread shutdownHook = null;
    protected boolean useNaming = true;
    protected boolean loaded = false;

    public boolean isUseShutdownHook() {
        return useShutdownHook;
    }

    public void setUseShutdownHook(boolean useShutdownHook){
        this.useShutdownHook = useShutdownHook;
    }

    public ClassLoader getParentClassLoader() {
        return parentClassLoader;
    }

    public void setParentClassLoader(ClassLoader parentClassLoader) {
        this.parentClassLoader = parentClassLoader;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
    public boolean isUseNaming(){
        return this.useNaming;
    }
    public void setUseNaming(boolean useNaming) {
        this.useNaming = useNaming;
    }

    public void setAwait(boolean await) {
        this.await = await;
    }

    public boolean isAwait() {
        return await;
    }
}
