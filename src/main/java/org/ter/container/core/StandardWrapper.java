package org.ter.container.core;


import org.ter.container.Container;
import org.ter.container.Context;
import org.ter.container.Wrapper;
import org.ter.container.pipeline.StandardWrapperValve;
import org.ter.lifecycle.LifecycleState;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StandardWrapper extends ContainerBase implements ServletConfig, Wrapper {
    private Container parent;
    /**
     * 此Servlet的参数
     */
    protected HashMap<String,String> parameters = new HashMap<>();
    protected final ArrayList<String> mappings = new ArrayList<>();
    private final ReentrantReadWriteLock mappingsLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock parametersLock = new ReentrantReadWriteLock();
    public StandardWrapper(){
        pipeline.setBasic(new StandardWrapperValve());
    }
    @Override
    public Container getParent() {
        return parent;
    }

    @Override
    public void setParent(Container parent) {
        this.parent = parent;
    }

    @Override
    public String getServletName() {
        return getName();
    }
    @Override
    public ServletContext getServletContext() {
        if(Objects.isNull(parent)){
            return null;
        }else if(!(parent instanceof Context)){
            return null;
        }else{
            return ((Context)parent).getServletContext();
        }
    }
    @Override
    public String getInitParameter(String s) {
        parametersLock.readLock().lock();
        try {
            return parameters.get(s);
        }finally {
            parametersLock.readLock().unlock();
        }
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        parametersLock.readLock().lock();
        try {
            return Collections.enumeration(parameters.keySet());
        }finally {
            parametersLock.readLock().unlock();
        }
    }

    @Override
    public Servlet getServlet() {
        return null;
    }

    @Override
    public void addMapping(String mapping) {

        mappingsLock.writeLock().lock();
        try {
            mappings.add(mapping);
        } finally {
            mappingsLock.writeLock().unlock();
        }
        if (parent.getLifecycleState().equals(LifecycleState.STARTED)) {
            fireContainerEvent(ADD_MAPPING_EVENT, mapping);
        }

    }
    @Override
    public void removeMapping(String mapping) {

        mappingsLock.writeLock().lock();
        try {
            mappings.remove(mapping);
        } finally {
            mappingsLock.writeLock().unlock();
        }
        if (parent.getLifecycleState().equals(LifecycleState.STARTED)) {
            fireContainerEvent(REMOVE_MAPPING_EVENT, mapping);
        }
    }
    @Override
    public String[] findMappings() {

        mappingsLock.readLock().lock();
        try {
            return mappings.toArray(new String[0]);
        } finally {
            mappingsLock.readLock().unlock();
        }
    }
}
