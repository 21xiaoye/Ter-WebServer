package org.ter.lifecycle;

import org.ter.container.*;
import org.ter.container.mapper.Mapper;
import org.ter.exception.LifecycleException;

import java.util.Objects;

public class MapperListener extends LifecycleBase implements ContainerListener, LifecycleListener{
    /**
     * 关联的映射器
     */
    private final Mapper mapper;
    /**
     * 关联的服务
     */
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
        findHostName();
        addListener(engine);

        Container[] children = engine.findChildren();
        for (Container container : children){
            Host host = (Host) container;
            if(!LifecycleState.NEW.equals(host.getLifecycleState())){
                registerHost(host);
            }
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
    private void findHostName(){
        Engine engine = service.getContainer();
        String defaultHost = engine.getDefaultHost();

        Container[] children = engine.findChildren();
        for (Container container : children){
            Host host = (Host) container;
            if(defaultHost.equalsIgnoreCase(host.getName())){
                mapper.setHostName(defaultHost);
                break;
            }
        }
    }
    private void addListener(Container container){
        container.addContainerListener(this);
        container.addLifecycleListener(this);
        Container[] children = container.findChildren();
        for (Container child: children) {
            addListener(child);
        }
    }
    private void registerHost(Host host){
        mapper.addHost(host.getName(), host);
        Container[] children = host.findChildren();
        for (Container container : children){
            Context context = (Context) container;
            if(context.getLifecycleState().isAvailable()){
                mapper.addHostMappingContext(host, context);
                registerContext(context);
            }
        }
    }

    private void registerContext(Context context){
        String path = context.getPath();
        if("/".equals(path)){
            path = "";
        }
        mapper.addContext(path, context);
        Container[] children = context.findChildren();
        for (Container container : children) {
            Wrapper wrapper = (Wrapper) container;
            String[] mappings = wrapper.findMappings();
            for (String mapping : mappings) {
                if("/".equals(mapping)){
                    context.setDefaultWrapper(wrapper);
                }else{
                    context.addWrapper(mapping, wrapper);
                }
            }
        }
    }
}
