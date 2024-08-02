package org.ter.lifecycle;

import org.ter.container.Container;
import org.ter.container.Engine;
import org.ter.container.Service;
import org.ter.container.mapper.Mapper;
import org.ter.exception.LifecycleException;

import java.util.Objects;

public class MapperListener extends LifecycleBase implements ContainerListener, LifecycleListener{
    private final Mapper mapper;
    private final Service service;
    public MapperListener(Service service){
        this.service = service;
        this.mapper = service.getMapper();
    }
    @Override
    public void containerEvent(ContainerEvent event) {

    }

    @Override
    public LifecycleState getLifecycleState() {
        return null;
    }

    @Override
    protected void initInternal() throws LifecycleException {
           System.out.println("初始化映射器......");
    }

    @Override
    protected void startInternal() throws LifecycleException {
        System.out.println("启动映射器......");
        setLifecycleState(LifecycleState.STARTING);
        Engine engine = service.getContainer();
        if(Objects.isNull(engine)){
            return;
        }
        addListener(engine);
    }

    @Override
    protected void stopInternal() throws LifecycleException {

    }

    @Override
    protected void destroyInternal() throws LifecycleException {

    }

    @Override
    public void lifecycleEvent(LifecycleEvent event) {

    }
    private void addListener(Container container){
        container.addContainerListener(this);
        container.addLifecycleListener(this);
        Container[] children = container.findChildren();
        for (Container child: children) {
            addListener(child);
        }
    }
}
