package org.ter.container.mapper;


import org.ter.connector.MappingData;
import org.ter.container.Container;
import org.ter.container.Context;
import org.ter.container.Host;
import org.ter.container.Wrapper;

import java.awt.geom.Area;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Mapper组件的主要作用是将传进来的 HTTP 请求的URL映射到相对应的Web程序
 * 、Servlet当中去。
 */
public class Mapper {
    /**
     * 主机名称
     */
    private String hostName;
    private Host host;
    private String contextPath;
    private Context context;
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
    public void addHostMappingContext(Host host, Context context){

    }
    public void addHost(String hostName, Host host){
        this.hostName = hostName;
        this.host = host;
    }
    public void addContext(String contextPath, Context context){
        this.contextPath = contextPath;
        this.context = context;
    }

    public Host getHost() {
        return host;
    }

    public void map(String hostName, String uri, MappingData mappingData) throws IOException {
        if(!hostName.equals(hostName)){
            return;
        }
        if(Objects.isNull(uri)){
            return;
        }
        internalMap(uri, mappingData);
    }
    private void internalMap(String uri,MappingData mappingData){
        mappingData.host = host;
        mappingData.context = context;
        mappingData.contextPath = contextPath;
        mappingData.wrapper = context.getWrapper(uri);
        String[] mappings = mappingData.wrapper.findMappings();
        for (String mapping : mappings) {
            if(mapping.equals(uri)){
                mappingData.wrapperPath = mapping;
                mappingData.requestPath = mapping;
                return;
            }
        }
        mappingData.wrapperPath = uri;
        mappingData.requestPath = uri;
    }
}