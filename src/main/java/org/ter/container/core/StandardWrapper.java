package org.ter.container.core;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import org.ter.container.Container;
import org.ter.container.Context;
import org.ter.container.Wrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StandardWrapper extends ContainerBase implements ServletConfig, Wrapper {
    /**
     * 父容器，只能是Context
     */
    private Container parent;
    /**
     * 此Servlet的参数
     */
    protected HashMap<String,String> parameters = new HashMap<>();
    private final ReentrantReadWriteLock parametersLock = new ReentrantReadWriteLock();

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
}
