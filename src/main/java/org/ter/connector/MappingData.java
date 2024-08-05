package org.ter.connector;

import org.ter.container.Context;
import org.ter.container.Host;
import org.ter.container.Wrapper;

import javax.swing.plaf.PanelUI;

public class MappingData {
    public Host host;
    public Context context;
    public Wrapper wrapper;
    public String contextPath;
    public String wrapperPath;
    public String requestPath;
    public void recycle(){
        host = null;
        context = null;
        wrapper = null;
        contextPath = null;
        wrapperPath =  null;
        requestPath = null;
    }
}
