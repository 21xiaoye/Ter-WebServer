package org.ter.startup;

import jakarta.servlet.Servlet;
import org.ter.connector.Connector;
import org.ter.container.*;
import org.ter.container.core.*;
import org.ter.exception.LifecycleException;
import org.ter.util.res.StringManager;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class Ter {
    private static final StringManager sm = StringManager.getStringManager(Ter.class);
    protected int port = 8080;
    protected String address ="localhost";
    protected Server server;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Server getServer() {
        if(Objects.nonNull(server)){
            return this.server;
        }
        this.server = new StandardServer();
        server.setPort(-1);

        StandardService service = new StandardService();
        service.setName("Ter");
        this.server.addService(service);
        return this.server;
    }

    public Service getService(){
        return this.server.findService();
    }

    public Connector getConnector(){
        Service service = getService();
        if(service.findConnectors().length > 0){
            return service.findConnectors()[0];
        }
        Connector connector = new Connector();
        connector.setPort(getPort());
        service.addConnector(connector);
        return connector;
    }
    public void start() throws LifecycleException {
        getServer();
        getConnector();
        this.server.start();
    }
    public Context addContext(String contextPath, String docBase){
        return addContext(getHost(), contextPath, docBase);
    }
    public Context addContext(Host host, String contextPath, String dir){
        return addContext(host, contextPath, contextPath, dir);
    }
    private Context addContext(Host host, String contextPath, String contextName, String dir){
        Context context = createContext(host, contextPath);
        context.setName(contextName);
        context.setPath(contextPath);
        context.setDocBase(dir);
        if(Objects.nonNull(host)){
            getHost().addChild(context);
        }else {
            host.addChild(context);
        }
        return context;
    }

    public Host getHost(){
        Engine engine = getEngine();
        if(engine.findChildren().length >0){
            return (Host)engine.findChildren()[0];
        }
        StandardHost host = new StandardHost();
        host.setName(this.address);
        engine.addChild(host);
        return host;
    }
    private Context createContext(Host host, String url){
        String contextClass = StandardContext.class.getName();
        if(Objects.isNull(host)){
            host = getHost();
        }
        if(host instanceof StandardHost){
            contextClass = ((StandardHost) host).getContextClass();
        }
        try {
            return (Context) Class.forName(contextClass).getConstructor().newInstance();
        } catch (ClassNotFoundException | InvocationTargetException
                 | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    public Engine getEngine(){
        Service service = getServer().findService();
        if(Objects.nonNull(service.getContainer())){
            return service.getContainer();
        }
        Engine engine  = new StandardEngine();
        engine.setName("Ter");
        engine.setDefaultHost(this.address);
        service.setContainer(engine);
        return engine;
    }

    /**
     * 添加Servlet到Context上下文当中
     *
     * @param ctx   需要添加的Servlet上下文
     * @param servletName   Servlet名称
     * @param servlet   要添加的Servlet
     * @return  返回此Servlet包装器
     */
    public  Wrapper addServlet(Context ctx, String servletName, Servlet servlet) {
        Wrapper sw = new ExistingStandardWrapper(servlet);
        sw.setName(servletName);
        ctx.addChild(sw);

        return sw;
    }
}
