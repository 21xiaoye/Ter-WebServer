package org.ter.container.core;

import org.ter.container.Server;
import org.ter.container.Service;
import org.ter.exception.LifecycleException;
import org.ter.lifecycle.LifecycleBase;
import org.ter.lifecycle.LifecycleState;
import org.ter.startup.Catalina;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
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
     * 在本服务器的服务
     */
    private Service service = null;
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
        if(Objects.nonNull(this.parentClassLoader)){
            return this.parentClassLoader;
        }
        if(Objects.isNull(this.catalina)){
            this.catalina.getParentClassLoader();
        }
        return ClassLoader.getSystemClassLoader();
    }

    /**
     * 设置新的父类加载器
     *
     * @param loader 新的父类加载器
     */
    @Override
    public void setParentClassLoader(ClassLoader loader) {
        ClassLoader classLoader = this.parentClassLoader;
        this.parentClassLoader = loader;
        support.firePropertyChange("parentClassLoader", classLoader, loader);
    }

    /**
     * 添加新的服务到服务集合当中
     *
     * @param service  新的服务
     */
    @Override
    public void addService(Service service) {
        synchronized (serviceLock){
            this.service = service;
            support.firePropertyChange("service", null, service);
        }
    }

    @Override
    public Catalina getCatalina() {
        return catalina;
    }

    @Override
    public void setCatalina(Catalina catalina) {
        this.catalina = catalina;
    }

    @Override
    public void await() {
        Thread currentThread = Thread.currentThread();
        if (getPort() == -1) {
            try {
                awaitThread = currentThread;
                while (!stopAwait) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        // continue and check the flag
                    }
                }
            } finally {
                awaitThread = null;
            }
            return;
        }
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
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                }
            }
        }
    }

    @Override
    public Service findService() {
        return this.service;
    }


    @Override
    public void removeService() {
        synchronized (serviceLock){
            try {
                this.service.stop();
            }catch (LifecycleException exception){

            }
            this.service = null;
            support.firePropertyChange("service", service, null);
        }
    }

    @Override
    protected void initInternal() throws LifecycleException {
        System.out.println("初始化服务器......");
        synchronized (serviceLock){
            this.service.init();
        }
    }

    @Override
    protected void startInternal() throws LifecycleException {
        System.out.println("启动服务器......");
        fireLifecycleEvent(CONFIGURE_START_EVENT, null);
        setLifecycleState(LifecycleState.STARTING);
        synchronized (serviceLock){
            this.service.start();
        }
    }

    @Override
    protected void stopInternal() throws LifecycleException {
        setLifecycleState(LifecycleState.STOPPING);
        fireLifecycleEvent(CONFIGURE_STOP_EVENT, null);
        synchronized (serviceLock){
            this.service.stop();
        }
        this.stopAwait();
    }

    @Override
    protected void destroyInternal() throws LifecycleException {
        synchronized (serviceLock){
            this.service.destroy();
        }
    }

    private void stopAwait(){
        this.stopAwait = true;
        Thread t = this.awaitThread;
        if(Objects.nonNull(t)){
            ServerSocket socket = this.awaitSocket;
            if(Objects.nonNull(socket)){
                this.awaitSocket = null;
                try {
                    socket.close();
                }catch (IOException exception){

                }
            }
            t.interrupt();
            try {
                t.join(1000);
            }catch (InterruptedException exception){

            }
        }
    }
}
