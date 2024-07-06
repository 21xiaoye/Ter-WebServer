package org.ter.container.core;

import org.ter.container.Container;
import org.ter.container.Context;
import org.ter.container.Host;
import org.ter.exception.LifecycleException;
import org.ter.lifecycle.LifecycleEvent;
import org.ter.lifecycle.LifecycleListener;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

public class StandardHost extends ContainerBase implements Host {
    private final Map<ClassLoader, String> childClassLoader = new WeakHashMap<>();
    private String contextClass = "org/ter/container/core/StandardContext";
    public StandardHost(){

    }
    @Override
    public void setName(String name) {
        if(Objects.isNull(name)){
            throw new IllegalArgumentException(sm.getString("standardHost.nullName"));
        }
        super.setName(name.toLowerCase(Locale.ENGLISH));
    }
    @Override
    public void addChild(Container container) {
        if(!(container instanceof Context)){
            throw new IllegalArgumentException(sm.getString("standardHost.notContext"));
        }
        container.addLifecycleListener(new MemoryLeakTrackingListener());

        super.addChild(container);
    }
    public String getContextClass() {
        return this.contextClass;
    }
    private class MemoryLeakTrackingListener implements LifecycleListener{
        @Override
        public void lifecycleEvent(LifecycleEvent event) {
            if(event.getType().equals(AFTER_START_EVENT)){
                if(event.getSource() instanceof Context){
                    Context context = ((Context) event.getSource());
                }
            }
        }
    }

    @Override
    protected void startInternal() throws LifecycleException {
        System.out.println("启动Host......");

        super.startInternal();
    }

}
