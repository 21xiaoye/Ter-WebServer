package org.ter.container;

import org.ter.lifecycle.Lifecycle;
import org.ter.startup.Catalina;

/**
 * 元素 Server 表示整个 Catalina servlet 容器。
 * 其属性表示整个 servlet 容器的特征。A Server
 * 可以包含一个或多个 Services，以及顶级命名资源集.
 *
 * 主要管理服务器的生命周期和配置
 */
public interface Server extends Lifecycle {
    /**
     * 获取侦听关闭命令的端口号
     *
     * @return 端口号
     */
    int getPort();

    /**
     * 设置侦听关闭命令的端口号
     *
     * @param port  新的端口号
     */
    void setPort(int port);

    /**
     * 获取侦听关闭命令的地址
     *
     * @return  关闭命令的地址
     */
    String getAddress();

    /**
     * 设置侦听关闭命令的地址
     *
     * @param address   新的地址
     */
    void setAddress(String address);

    /**
     * 获取关机命令
     *
     * @return  关机命令
     */
    String getShutdown();

    /**
     * 设置关闭命令的关闭命令
     *
     * @param shutdown  新的关机命令
     */
    void setShutdown(String shutdown);

    /**
     * 获取此组件父类加载器
     *
     * @return 此组件的父类加载器
     */
    ClassLoader getParentClassLoader();

    /**
     * 设置此组件父类加载器
     *
     * @param loader 新的父类加载器
     */
    void setParentClassLoader(ClassLoader loader);

    /**
     * 添加新服务到服务集当中
     *
     * @param service  新的服务
     */
    void addService(Service service);

    /**
     * 等待真正的关机命令
     */
    void await();

    /**
     * 查找本服务器上的服务
     *
     * @return  返回查找的服务
     */
    Service findService();

    /**
     * 删除此服务器上相关联的服务
     */
    void removeService();

    /**
     * 返回外部 Catalina 启动/关机组件（如果存在）。
     *
     * @return 返回外部 Catalina
     */
    Catalina getCatalina();

    /**
     * 设置外部 Catalina
     *
     * @param catalina 新的Catalina
     */
    void setCatalina(Catalina catalina);
}