package org.ter.container.core;

import org.ter.container.Container;
import org.ter.container.Context;
import org.ter.container.Host;
import org.ter.container.Valve;
import org.ter.container.pipeline.StandardHostValve;
import org.ter.exception.LifecycleException;
import org.ter.lifecycle.LifecycleEvent;
import org.ter.lifecycle.LifecycleListener;
import org.ter.lifecycle.LifecycleState;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

public class StandardHost extends ContainerBase implements Host {
    private final Map<ClassLoader, String> childClassLoader = new WeakHashMap<>();
    private String contextClass = "org.ter.container.core.StandardContext";
    private String errorReportValveClass = "org.ter.container.pipeline.ErrorReportValve";
    public StandardHost(){
        pipeline.setBasic(new StandardHostValve());
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
    protected void initInternal() throws LifecycleException {
        System.out.println("初始化Host......");
        super.initInternal();
    }

    @Override
    protected void startInternal() throws LifecycleException {
        System.out.println("启动Host......");
        String valveClass = getErrorReportValveClass();
        try {

            if (!getPipeline().findValve(valveClass)) {
                Valve valve = (Valve) Class.forName(valveClass).getConstructor().newInstance();
                getPipeline().addValve(valve);
            }
        }catch (Throwable throwable){
            //记录日志
        }
        setLifecycleState(LifecycleState.STARTING);
        super.startInternal();
    }
    public String getErrorReportValveClass() {
        return errorReportValveClass;
    }
}
