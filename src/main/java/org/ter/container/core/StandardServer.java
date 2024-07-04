package org.ter.container.core;

import org.ter.container.Server;
import org.ter.container.Service;
import org.ter.exception.LifecycleException;
import org.ter.lifecycle.LifecycleBase;
import org.ter.startup.Catalina;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Objects;

public class StandardServer extends LifecycleBase implements Server {
    /**
     * 用于等待关机的端口号
     */
    private int port = 8005;
    /**
     * 关机命令的地址
     */
    private String address = "localhost";
    /**
     * 在本服务器的服务集合
     */
    private Service[] services = new Service[0];
    private final Object serviceLock = new Object();
    /**
     * 关机命令字符串
     */
    private String shutdown = "SHUTDOWN";
    /**
     * 监听属性变化
     */
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    /**
     * 服务器状态
     */
    private volatile boolean stopAwait = false;
    private Catalina catalina = null;
    /**
     * 父类加载器
     */
    private ClassLoader parentClassLoader = null;
    /**
     * 用于await()的线程
     */
    private volatile Thread awaitThread = null;
    /**
     * 用于等待 shutdown 命令的服务器套接字
     */
    private volatile ServerSocket awaitSocket = null;
    public StandardServer(){
        super();
    }
    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String getAddress() {
        return this.address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 返回正在等待的 shutdown命令字符串
     *
     * @return shutdown命令字符串
     */
    @Override
    public String getShutdown() {
        return this.shutdown;
    }

    /**
     * 设置新地关机字符串
     *
     * @param shutdown 新地关机命令
     */
    @Override
    public void setShutdown(String shutdown) {
        this.shutdown = shutdown;
    }

    /**
     * 获取父类加载器
     *
     * @return 父类加载器
     */
    @Override
    public ClassLoader getParentClassLoader() {
        return this.parentClassLoader;
    }

    /**
     * 设置新的父类加载器
     *
     * @param loader 新的父类加载器
     */
    @Override
    public void setParentClassLoader(ClassLoader loader) {
        this.parentClassLoader = loader;
    }

    /**
     * 添加新的服务到服务集合当中
     *
     * @param service  新的服务
     */
    @Override
    public void addService(Service service) {
        service.setServer(this);
        synchronized (serviceLock){
            Service[] newServices = new Service[this.services.length + 1];
            System.arraycopy(this.services, 0, newServices, 0, this.services.length);
            newServices[this.services.length] = service;
            this.services = newServices;
            if(getLifecycleState().isAvailable()){
               try {
                   service.start();
               }catch (LifecycleException exception){
                   throw new RuntimeException(exception);
               }
            }
            support.firePropertyChange("service", null, service);
        }
    }

    @Override
    public void await() {
        Thread currentThread = Thread.currentThread();
        try {
            this.awaitSocket = new ServerSocket(getPort(), 1, InetAddress.getByName(this.address));
        }catch (IOException exception){
            throw new RuntimeException(exception);
        }

        try {
            this.awaitThread = currentThread;
            while (!stopAwait){
                ServerSocket serverSocket = this.awaitSocket;
                if(Objects.isNull(serverSocket)){
                    break;
                }
                Socket socket = null;
                StringBuilder command = new StringBuilder();
                try {
                    InputStream stream;
                    try {
                        socket = serverSocket.accept();
                        socket.setSoTimeout(10 * 1000);
                        stream = socket.getInputStream();
                    }catch (SocketTimeoutException exception){
                        continue;
                    }catch (IOException exception){
                        if(stopAwait){
                            break;
                        }else{
                            System.out.println(command);
                        }
                        break;
                    }
                    int expected = 1024;
                    // 读取管道数据
                    while (expected > 0){
                        int ch = -1;
                        try {
                            ch = stream.read();
                        }catch (IOException exception){
                            ch = -1;
                        }
                        if(ch < 32 || ch == 127){
                            break;
                        }
                        command.append((char) ch);
                        expected--;
                    }
                }finally {
                    try {
                        if(Objects.nonNull(socket)){
                            socket.close();
                        }
                    }catch (IOException exception){

                    }
                }
                boolean match = command.toString().equals(shutdown);
                if(match){
                    break;
                }
            }
        }finally {
            ServerSocket serverSocket = awaitSocket;
            awaitThread = null;
            awaitSocket = null;

            // Close the server socket and return
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Override
    public Service findService(String name) {
        if(Objects.isNull(name)){
            return null;
        }
        synchronized (serviceLock){
            for (Service service : this.services){
                if(service.equals(service.getName())){
                    return service;
                }
            }
        }
        return null;
    }

    @Override
    public Service[] findService() {
        synchronized (serviceLock){
            return this.services.clone();
        }
    }

    @Override
    public void removeService(Service service) {
        synchronized (serviceLock){
            int currentIndex = -1;
            for (int i = 0; i < this.services.length; i++) {
                if(service == this.services[i]){
                    currentIndex = i;
                    break;
                }
            }
            if(currentIndex < 0){
                return;
            }
            try {
                services[currentIndex].stop();
            }catch (LifecycleException exception){

            }
            int k = 0;
            Service[] newServices = new Service[this.services.length - 1];
            for (int i = 0; i < services.length; i++) {
                if(i != currentIndex){
                    newServices[k++] = this.services[i];
                }
            }
            this.services = newServices;
            support.firePropertyChange("service", service, null);
        }
    }

    @Override
    protected void initInternal() throws LifecycleException {

    }

    @Override
    protected void startInternal() throws LifecycleException {

    }

    @Override
    protected void stopInternal() throws LifecycleException {

    }

    @Override
    protected void destroyInternal() throws LifecycleException {

    }
}
