package org.ter.lifecycle;

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

    }

    @Override
    protected void startInternal() throws LifecycleException {
        setState(LifecycleState.STARTING);
        Engine engine = service.getContainer();
        if(Objects.isNull(engine)){
            return;
        }
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
}
