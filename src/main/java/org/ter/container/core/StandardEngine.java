package org.ter.container.core;

import org.ter.container.Container;
import org.ter.container.Engine;
import org.ter.container.Host;
import org.ter.container.Service;
import org.ter.exception.LifecycleException;
import org.ter.lifecycle.LifecycleListener;


import java.util.Locale;
import java.util.Objects;

public class StandardEngine extends ContainerBase implements Engine {
    private String defaultHost = null;
    private Service service = null;
    public StandardEngine(){

    }
    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return new LifecycleListener[0];
    }
    @Override
    public String getDefaultHost() {
        return this.defaultHost;
    }
    @Override
    public void setDefaultHost(String defaultHost) {
        if(Objects.isNull(defaultHost)){
            this.defaultHost = null;
        }else{
            this.defaultHost = defaultHost.toLowerCase(Locale.ENGLISH);
        }
        if(getLifecycleState().isAvailable()){

        }
    }

    @Override
    public Service getService() {
        return this.service;
    }

    @Override
    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public void addChild(Container container) {
        // 引擎的子容器只能为Host
        if(!(container instanceof Host)){
            throw new IllegalArgumentException(sm.getString("standardEngine.notHost"));
        }
        super.addChild(container);
    }

    /**
     * 引擎位于容器层次结构的顶部,
     * 不能为其添加父容器
     *
     * @param container 将此容器作为子容器添加到的容器
     */
     @Override
    public void setParent(Container container) {
        throw new IllegalArgumentException(sm.getString("standardEngine.notPARENT"));
    }

    @Override
    protected void initInternal() throws LifecycleException {
        System.out.println("初始化Engine容器......");
         super.initInternal();
    }

    @Override
    protected void startInternal() throws LifecycleException {
        System.out.println("启动Engine容器......");
        super.startInternal();
    }

    @Override
    public ClassLoader getParentClassLoader() {
         if(Objects.nonNull(parentClassLoader)){
             return parentClassLoader;
         }
         if(Objects.nonNull(service)){
             return service.getParentClassLoader();
         }
        return ClassLoader.getSystemClassLoader();
    }
}
