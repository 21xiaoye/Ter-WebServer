package org.ter.startup;

import org.ter.connector.Connector;
import org.ter.container.*;
import org.ter.container.core.*;
import org.ter.exception.LifecycleException;
import org.ter.ter_server.util.res.StringManager;

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
        service.addConnector(connector);
        return connector;
    }
    public void start() throws LifecycleException {
        getServer();
        getConnector();
        this.server.start();
    }

    public Context addContext(Host host, String contextPath, String dir){
        return addContext(host, contextPath, contextPath, dir);
    }
    private Context addContext(Host host, String contextPath, String contextName, String dir){
        Context context = createContext(host, contextPath);
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
        return null;
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
}
