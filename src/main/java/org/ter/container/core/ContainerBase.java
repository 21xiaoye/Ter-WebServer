package org.ter.container.core;

import org.ter.container.Container;
import org.ter.container.Pipeline;
import org.ter.exception.LifecycleException;
import org.ter.lifecycle.*;
import org.ter.ter_server.util.res.StringManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class ContainerBase extends LifecycleBase implements Container {
    protected static final StringManager sm = StringManager.getStringManager(ContainerBase.class);
    /**
     * 拥有此容器的所有子容器
     */
    protected final HashMap<String, Container> containers = new HashMap<>();
    /**
     * 此容器的容器事件侦听器
     */
    protected final List<ContainerListener> listeners = new CopyOnWriteArrayList<>();
    /**
     * 容器名称
     */
    protected String name;
    /**
     * 此容器为其子容器的父容器
     */
    protected Container parent = null;
    /**
     * 此容器的父类加载器，配置loader时需配置父类记载器
     */
    protected ClassLoader parentClassLoader = null;
    /**
     * 与此容器关联的 Pipeline对象
     */
    protected final Pipeline pipeline = new StandardPipeline(this);
    /**
     * 添加子容器时，是否启动子容器
     */
    protected boolean startChildren = true;
    /**
     * 增加对此组件的属性更改支持
     */
    protected final PropertyChangeSupport support = new PropertyChangeSupport(this);
    /**
     * 线程池
     */
    protected ThreadPoolExecutor startStopExecutor;
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        if(Objects.isNull(name)){
            throw new IllegalArgumentException(sm.getString("containerBase.nullName"));
        }
        String oldName = this.name;
        this.name = name;
        support.firePropertyChange("name", oldName, name);
    }

    /**
     * @return 如果此容器的子项在添加到此容器时将自动启动，则返回此值
     */
    public boolean isStartChildren() {
        return this.startChildren;
    }
    public void setStartChildren(boolean startChildren){
        boolean oldStartChildren = this.startChildren;
        this.startChildren = startChildren;
        support.firePropertyChange("startChildren", oldStartChildren, startChildren);
    }
    @Override
    public Container getParent() {
        return this.parent;
    }
    @Override
    public void setParent(Container container) {
        Container oldParent = this.parent;
        this.parent = parent;
        support.firePropertyChange("parent", oldParent, container);
    }

    @Override
    public ClassLoader getParentClassLoader() {
        if(Objects.nonNull(parentClassLoader)){
            return this.parentClassLoader;
        }
        if(Objects.nonNull(parent)){
            return this.parent.getParentClassLoader();
        }
        return ClassLoader.getSystemClassLoader();
    }

    @Override
    public void setParentClassLoader(ClassLoader parentClassLoader) {
        ClassLoader oldParentClassLoader = this.parentClassLoader;
        this.parentClassLoader = parentClassLoader;
        this.support.firePropertyChange("parentClassLoader", oldParentClassLoader, parentClassLoader);
    }

    @Override
    public Pipeline getPipeline() {
        return this.pipeline;
    }

    @Override
    public void addChild(Container container) {
        addChildInternal(container);
    }
    private void addChildInternal(Container container){
        synchronized (containers){
            if(Objects.nonNull(containers.get(container.getName()))){
                throw new IllegalArgumentException(sm.getString("containerBase.child.notUnique",container.getName()));
            }
            container.setParent(this);
            this.containers.put(container.getName(), container);
        }
        // 启动容器
        try {
            if(startChildren && (getLifecycleState().isAvailable() || LifecycleState.STARTING_PREP.equals(getLifecycleState()))){
                container.start();
            }
        }catch (LifecycleException exception){
            throw new IllegalStateException(sm.getString("containerBase.containers"), exception);
        }finally {
            fireContainerEvent(ADD_CHILD_EVENT, container);
        }
    }
    @Override
    public void addContainerListener(ContainerListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    @Override
    public Container findChild(String name) {
        if(Objects.isNull(name)){
            return null;
        }
        synchronized (this.containers){
            return this.containers.get(name);
        }
    }

    @Override
    public Container[] findChildren() {
        synchronized (this.containers){
            return containers.values().toArray(new Container[0]);
        }
    }

    @Override
    public ContainerListener[] findContainerListeners() {
        return this.listeners.toArray(new ContainerListener[0]);
    }

    @Override
    public void removeChild(Container container) {
        if(Objects.isNull(container)){
            return;
        }
        try {
            if(container.getLifecycleState().isAvailable()){
                container.stop();
            }
        }catch (LifecycleException exception){

        }

        try {
            if(!LifecycleState.DESTROYING.equals(container.getLifecycleState())){
                container.destroy();
            }
        }catch (LifecycleException exception){

        }
        synchronized (this.containers){
            if(Objects.isNull(this.containers.get(container.getName()))){
                return;
            }
            this.containers.remove(container.getName());
        }
        fireContainerEvent(REMOVE_CHILD_EVENT, container);
    }

    @Override
    public void removeContainerListener(ContainerListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.support.removePropertyChangeListener(listener);
    }

    @Override
    public void fireContainerEvent(String type, Object data) {
        if(this.listeners.size() < 1){
            return;
        }
        ContainerEvent containerEvent = new ContainerEvent(this, type, data);
        for (ContainerListener listener : this.listeners) {
            listener.containerEvent(containerEvent);
        }
    }

    @Override
    protected void initInternal() throws LifecycleException {
        LinkedBlockingQueue<Runnable> startStopQueue = new LinkedBlockingQueue<>();
        this.startStopExecutor = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, startStopQueue);
        this.startStopExecutor.allowCoreThreadTimeOut(true);
    }

    @Override
    protected void startInternal() throws LifecycleException {
        Container[] children = findChildren();
        for (Container container : children) {
            this.startStopExecutor.submit(new StartChild(container));
        }

        if(this.pipeline instanceof Lifecycle){
            ((Lifecycle) this.pipeline).start();
        }
        setLifecycleState(LifecycleState.STARTING);
    }

    @Override
    protected void stopInternal() throws LifecycleException {

    }

    @Override
    protected void destroyInternal() throws LifecycleException {

    }

    private static class StartChild implements Callable<Void>{
        private Container container;
        StartChild(Container container){
            this.container = container;
        }
        @Override
        public Void call() throws Exception {
            container.start();
            return null;
        }
    }
}