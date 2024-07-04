package org.ter.container;

import org.ter.connector.Connector;
import org.ter.container.mapper.Mapper;
import org.ter.lifecycle.Lifecycle;

/**
 * 服务是一组一个或多个连接器，它们共享一个容器来处理其传入请求。
 * 可以包含多个Service实例，Service实例之间彼此独立
 */
public interface Service extends Lifecycle {
    /**
     * 获取此容器服务的引擎
     *
     * @return 此服务的引擎
     */
    Engine getContainer();

    /**
     * 设置与此服务关联的引擎
     *
     * @param engine 新引擎
     */
    void setContainer(Engine engine);

    /**
     * 获取服务名称
     *
     * @return 服务名称
     */
    String getName();

    /**
     * 设置服务名称
     *
     * @param name 服务名称
     */
    void setName(String name);

    /**
     * 获取与服务相关的服务器
     *
     * @return  与此服务相关的服务器
     */
    Server getServer();

    /**
     * 设置与此服务关联的服务器
     *
     * @param server 拥有此服务的服务器
     */
    void setServer(Server server);

    /**
     * 获取此服务的父类加载器，如果没有这返回getServer,
     * 如果没有设置服务器，则返回系统加载器
     *
     * @return  此组件的父类加载器
     */
    ClassLoader getParentClassLoader();

    /**
     * 设置组件的父类加载器
     *
     * @param classLoader 新的父类加载器
     */
    void setParentClassLoader(ClassLoader classLoader);

    /**
     * 此服务注册的域
     *
     * @return  此服务注册的域
     */
    String getDomain();

    /**
     * 添加新的连接器到连接器集合，
     * 将其与此服务相关联的所有容器相关联
     *
     * @param connector  新的连接器
     */
    void addConnector(Connector connector);

    /**
     * 获取此服务的连接器集
     *
     * @return  与此服务相关联的连接器集
     */
    Connector[] findConnectors();

    /**
     * 删除与此服务相关联的连接器，
     * 并移除与此服务相关联容器的连接
     *
     * @param connector 需要删除的连接器
     */
    void removeConnector(Connector connector);

    /**
     * 添加执行器到此服务
     *
     * @param executor 需要添加的执行器
     */
    void addExecutor(Executor executor);

    /**
     * 返回于此服务相关联的执行器集
     *
     * @return  执行器集
     */
    Executor[] findExecutors();

    /**
     * 根据名称返回执行器
     *
     * @param name  执行器名称
     * @return  执行器，没有返回Null
     */
    Executor getExecutor(String name);

    /**
     * 删除此服务的执行程序
     *
     * @param executor 需要删除的执行器
     */
    void removeExecutor(Executor executor);

    /**
     * 与此服务相关联的映射器
     *
     * @return  映射器
     */
    Mapper getMapper();
}
