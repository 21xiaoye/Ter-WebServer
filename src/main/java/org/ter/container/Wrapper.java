package org.ter.container;

import jakarta.servlet.Servlet;

public interface Wrapper extends Container{
    /**
     * 添加包装器的容器事件。
     */
    String ADD_MAPPING_EVENT = "addMapping";

    /**
     * 删除包装器的容器事件。
     */
    String REMOVE_MAPPING_EVENT = "removeMapping";
    /**
     * 获取关联的Servlet实例
     * @return 关联的Servlet实例
     */
    Servlet getServlet();

    /**
     * 删除与此包装器相关的映射
     * @param mapping
     */
    void removeMapping(String mapping);
    void addMapping(String mapping);
}
