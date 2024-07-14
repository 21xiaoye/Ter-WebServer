package org.ter.connector;

import org.ter.container.Service;
import org.ter.container.util.IntrospectionUtils;
import org.ter.container.util.StringUtil;
import org.ter.coyote.Adapter;
import org.ter.coyote.ProtocolHandler;
import org.ter.coyote.http1.Http11NioProtocol;
import org.ter.exception.LifecycleException;
import org.ter.lifecycle.LifecycleBase;
import org.ter.lifecycle.LifecycleState;
import org.ter.ter_server.util.res.StringManager;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

/**
 * 连接器实现
 */
public class Connector extends LifecycleBase {
    protected static final StringManager sm = StringManager.getStringManager(Connector.class);
    /**
     * 连接器关联的服务
     */
    protected Service service = null;
    /**
     * 异步请求超时时间
     */
    protected long asyncTimeout = 30000;
    /**
     * 侦听请求的端口号
     */
    protected int port = -1;
    /**
     * 对所有通过此连接器的请求设置请求方案
     */
    protected String scheme  = "http";
    /**
     * 对所有通过此连接器的请求设置安全标志
     */
    protected boolean secure = false;
    /**
     * 请求允许的最大 Cookie 数。使用小于零的值表示无限制。默认值为 200。
     */
    private int maxCookieCount = 200;
    /**
     * 容器自动解析的最大参数数（GET 加 POST）。默认为 10000。
     * 默认 Tomcat server.xml配置较低的默认值 1000。小于 0 的值表示没有限制。
     */
    protected int maxParameterCount = 10000;
    /**
     * 容器将自动解析的 POST 的最大大小。默认为 2 MiB。
     */
    protected int maxPostSize = 2 * 1024 * 1024;
    /**
     * 容器在身份验证期间保存的 POST 的最大大小。默认为 4 KiB
     */
    protected int maxSavePostSize = 4 * 1024;
    /**
     * 根据 application/x-www-form-urlencoded 请求正文的 POST 样式规则
     * 解析的 HTTP 方法的逗号分隔列表
     */
    protected String parseBodyMethods = "POST";
    protected HashSet<String> parseBodyMethodsSet;
    /**
     * URL编码
     */
    private Charset uriCharset = StandardCharsets.UTF_8;
    /**
     * Coyote 协议处理器
     */
    protected final ProtocolHandler protocolHandler;
    /**
     * 默认的Coyote 协议处理器
     */
    protected String protocolHandlerClassName = "org.ter.coyote.http1.Http11NioProtocol";
    /**
     * Coyote 适配器
     */
    protected Adapter adapter = null;
    public Connector(){
        this(null);
    }
    public Connector(String protocol){
        ProtocolHandler p = null;
        try {
            Class<?> aClass = Class.forName(protocolHandlerClassName);
            p = (ProtocolHandler) aClass.getConstructor().newInstance();
        }catch (Exception exception){
            // 日志记录
        }finally {
            this.protocolHandler = p;
        }
    }
    public Service getService() {
        return service;
    }
    public void setService(Service service) {
        this.service = service;
    }
    public long getAsyncTimeout() {
        return asyncTimeout;
    }
    public void setAsyncTimeout(long asyncTimeout) {
        this.asyncTimeout = asyncTimeout;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
        setProperty("port",String.valueOf(port));
    }
    public String getScheme() {
        return scheme;
    }
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
    public boolean getSecure(){
        return this.secure;
    }
    public void setSecure(boolean secure) {
        this.secure = secure;
    }
    public int getMaxCookieCount() {
        return maxCookieCount;
    }
    public void setMaxCookieCount(int maxCookieCount) {
        this.maxCookieCount = maxCookieCount;
    }
    public int getMaxParameterCount() {
        return maxParameterCount;
    }
    public void setMaxParameterCount(int maxParameterCount) {
        this.maxParameterCount = maxParameterCount;
    }
    public int getMaxPostSize() {
        return maxPostSize;
    }
    public void setMaxPostSize(int maxPostSize) {
        this.maxPostSize = maxPostSize;
    }
    public int getMaxSavePostSize() {
        return maxSavePostSize;
    }
    public void setMaxSavePostSize(int maxSavePostSize) {
        this.maxSavePostSize = maxSavePostSize;
    }
    public void setParseBodyMethods(String parseBodyMethods) {
        HashSet<String> methodsSet = new HashSet<>();
        if(Objects.nonNull(parseBodyMethods)){
            methodsSet.addAll(Arrays.asList(StringUtil.splitCommaSeparated(parseBodyMethods)));
        }
        if(methodsSet.contains("TRACE")){
            throw new IllegalArgumentException(sm.getString("coyoteConnector.parseBodyMethodNoTrace"));
        }
        this.parseBodyMethodsSet = methodsSet;
        this.parseBodyMethods = parseBodyMethods;
    }
    public String getParseBodyMethods() {
        return parseBodyMethods;
    }
    public HashSet<String> getParseBodyMethodsSet() {
        return parseBodyMethodsSet;
    }
    public void setParseBodyMethodsSet(HashSet<String> parseBodyMethodsSet) {
        this.parseBodyMethodsSet = parseBodyMethodsSet;
    }

    public Charset getUriCharset() {
        return uriCharset;
    }

    public void setUriCharset(Charset uriCharset) {
        this.uriCharset = uriCharset;
    }

    @Override
    protected void initInternal() throws LifecycleException {
        System.out.println("初始化连接器......");
        if(Objects.isNull(this.parseBodyMethodsSet)){
            setParseBodyMethods(getParseBodyMethods());
        }
        try {
            protocolHandler.init();
        }catch (Exception exception){
            throw new LifecycleException(sm.getString("coyoteConnector.protocolHandlerInitializationFailed"), exception);
        }
    }

    @Override
    protected void startInternal() throws LifecycleException {
        System.out.println("启动连接器......");
        setLifecycleState(LifecycleState.STARTING);
        try {
            protocolHandler.start();
        } catch (Exception exception) {
            throw new LifecycleException(sm.getString("coyoteConnector.protocolHandlerStartFailed"), exception);
        }
    }

    @Override
    protected void stopInternal() throws LifecycleException {

    }

    @Override
    protected void destroyInternal() throws LifecycleException {

    }

    /**
     * 在协议处理器上设置属性
     *
     * @param name  属性名称
     * @param value 属性值
     * @return  设置结果
     */
    public boolean setProperty(String name, String value) {
        return IntrospectionUtils.setProperty(protocolHandler, name, value);
    }
}
