package org.ter.container.core;


import org.ter.container.Context;
import org.ter.container.Wrapper;
import org.ter.container.util.URLEncoder;
import org.ter.exception.LifecycleException;
import org.ter.lifecycle.LifecycleState;

import javax.servlet.ServletContext;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;

public class StandardContext extends ContainerBase implements Context {
    /**
     * 此Web应用程序文档根目录
     */
    private String docBase = null;
    /**
     * 编码路径
     */
    private String encodePath = null;
    /**
     * 此Web应用程序上下文路径
     */
    private String path= null;
    /**
     * 此Context的Servlet映射，URL:Servlet名称
     */
    private HashMap<String,String> servletMappings = new HashMap<>();
    private final Object servletMappingsLock = new Object();
    private HashMap<String, Wrapper> wrapperMapping = new HashMap<>();
    private final Object wrapperMappingsLock = new Object();
    private Wrapper defaultWrapper = null;
    public StandardContext(){

    }
    @Override
    public String getDocBase() {
        return this.docBase;
    }

    @Override
    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }

    @Override
    public String getEncodedPath() {
        return this.encodePath;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public void setPath(String path) {
        boolean invalid = false;
        if(Objects.isNull(path) || "/".equals(path)){
            invalid = true;
            this.path = "";
        }else if(path.isEmpty() || path.startsWith("/")){
            this.path = path;
        }else{
            invalid  = true;
            this.path = "/" + path;
        }
        if(this.path.endsWith("/")){
            invalid = true;
            this.path = this.path.substring(0, this.path.length() - 1);
        }
        encodePath = URLEncoder.DEFAULT.encode(this.path, StandardCharsets.UTF_8);
        if(Objects.isNull(getName())){
            setName(this.path);
        }
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    protected void initInternal() throws LifecycleException {
        System.out.println("Context初始化......");
        super.initInternal();
    }

    @Override
    protected void startInternal() throws LifecycleException {
        System.out.println("Context启动......");
        setLifecycleState(LifecycleState.STARTING);
    }
    @Override
    public void addServletMappingDecoded(String pattern, String name) {
        synchronized (servletMappingsLock){
            String servletUrl = servletMappings.get(pattern);
            if(Objects.nonNull(servletUrl)){
                Wrapper wrapper = (Wrapper) findChild(pattern);
                wrapper.removeChild(wrapper);
            }
            servletMappings.put(pattern, name);
        }
        Wrapper wrapper = (Wrapper) findChild(name);
        wrapper.addMapping(pattern);
    }
    @Override
    public void setDefaultWrapper(Wrapper defaultWrapper) {
        this.defaultWrapper = defaultWrapper;
    }
    @Override
    public void addWrapper(String url, Wrapper wrapper){
        synchronized (wrapperMappingsLock){
            this.wrapperMapping.put(url, wrapper);
        }
    }
    public Wrapper getWrapper(String url){
        Wrapper wrapper = wrapperMapping.get(url);
        if(Objects.nonNull(wrapper)){
            return wrapper;
        }
        return defaultWrapper;
    }
}
