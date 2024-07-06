package org.ter.connector;

import org.ter.container.Service;
import org.ter.exception.LifecycleException;
import org.ter.lifecycle.LifecycleBase;
import org.ter.ter_server.util.res.StringManager;

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