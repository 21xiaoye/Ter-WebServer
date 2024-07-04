package org.ter.container.core;

import org.ter.connector.Connector;
import org.ter.container.Engine;
import org.ter.container.Executor;
import org.ter.container.Server;
import org.ter.container.Service;
import org.ter.container.mapper.Mapper;
import org.ter.exception.LifecycleException;
import org.ter.lifecycle.LifecycleBase;
import org.ter.lifecycle.LifecycleState;
import org.ter.lifecycle.MapperListener;
import org.ter.ter_server.util.res.StringManager;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * 服务的标准实现，关联的Container是Engine,
 * 一般一个服务对应一个Engine
 */
public class StandardService extends LifecycleBase implements Service {
    private static final StringManager sm = StringManager.getStringManager(StandardService.class);
    /**
     * 此服务的名称
     */
    private String name = null;
    /**
     * 此服务所在的服务器
     */
    private Server server = null;
    /**
     * 对此组件的属性进行更改，
     * 通知侦听器
     */
    protected final PropertyChangeSupport support = new PropertyChangeSupport(this);
    /**
     * 与此服务关联的连接器列表
     */
    protected Connector connectors[] = new Connector[0];
    private final Object connectorsLock = new Object();
    /**
     * 与此服务关联的执行器列表
     */
    protected final ArrayList<Executor> executors = new ArrayList<>();
    /**
     * 与此服务关联的Engine引擎
     */
    private Engine engine = null;
    /**
     * 父类加载器
     */
    private ClassLoader parentClassLoader = null;
    /**
     * 用来定义Servlet API映射规则
     */
    protected final Mapper mapper = new Mapper();
    /**
     * 请求映射侦听器
     */
    protected final MapperListener mapperListener = new MapperListener(this);
    @Override
    public LifecycleState getLifecycleState() {
        return null;
    }

    @Override
    protected void initInternal() throws LifecycleException {

    }

    @Override
    protected void startInternal() throws LifecycleException {

    }

    @Override
    protected void stopInternal() throws LifecycleException {

    }

    @Override
    protected void destroyInternal() throws LifecycleException {

    }
    @Override
    public Engine getContainer() {
        return this.engine;
    }
    @Override
    public void setContainer(Engine engine){
        this.engine = engine;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name =  name;
    }

    @Override
    public Server getServer() {
        return this.server;
    }

    @Override
    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public ClassLoader getParentClassLoader() {
        return this.parentClassLoader;
    }

    @Override
    public void setParentClassLoader(ClassLoader classLoader) {
        this.parentClassLoader = classLoader;
    }

    @Override
    public String getDomain() {
        return null;
    }

    @Override
    public void addConnector(Connector connector) {
        synchronized (connectorsLock){
            Connector[] newConnectors = new Connector[connectors.length + 1];
            System.arraycopy(connector, 0, newConnectors, 0, connectors.length);
            newConnectors[connectors.length] = connector;
            this.connectors = newConnectors;

            support.firePropertyChange("connector", null, connector);
        }
    }

    @Override
    public Connector[] findConnectors() {
        synchronized (connectorsLock){
            return connectors.clone();
        }
    }

    @Override
    public void removeConnector(Connector connector) {
        synchronized (connectorsLock){
            int currentIndex = -1;
            for (int i = 0; i < connectors.length; i++) {
                if(connector == connectors[i]){
                    currentIndex = i;
                    break;
                }
            }
            if(currentIndex < 0){
                return;
            }
        }
    }

    @Override
    public void addExecutor(Executor executor) {

    }

    @Override
    public Executor[] findExecutors() {
        return new Executor[0];
    }

    @Override
    public Executor getExecutor(String name) {
        return null;
    }

    @Override
    public void removeExecutor(Executor executor) {

    }

    @Override
    public Mapper getMapper() {
        return null;
    }
}
