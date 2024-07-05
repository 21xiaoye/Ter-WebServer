package org.ter.container;

/**
 * Servlet引擎
 */
public interface Engine extends Container{
    /**
     * 获取引擎默认的虚拟主机名
     *
     * @return 返回主机默认的虚拟主机名
     */
    String getDefaultHost();

    /**
     * 设置当前引擎的默认主机名
     *
     * @param defaultHost 新的默认主机名
     */
    void setDefaultHost(String defaultHost);

    /**
     * 获取引擎所在的服务
     *
     * @return 返回与引擎相关的服务
     */
    Service getService();

    /**
     * 设置引擎所属的服务
     *
     * @param service 拥有此引擎的服务
     */
    void setService(Service service);
}
