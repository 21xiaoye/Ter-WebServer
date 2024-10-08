package org.ter.container;


import javax.servlet.Servlet;
import javax.servlet.ServletException;

/**
 * Servlet包装器接口
 */
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
     *
     * @param mapping 需要删除的映射URL
     */
    void removeMapping(String mapping);

    /**
     * 添加于此包装器相关的映射
     *
     * @param mapping 需要添加到此包装器的URL
     */
    void addMapping(String mapping);

    /**
     * 获取当前Servlet Wrapper映射的URL列表
     *
     * @return 返回当前Servlet Wrapper映射的URL列表
     */
    String[] findMappings();

    /**
     * 分配初始化Servlet实例，并调用service()方法，
     * 如果Servlet为空，则调用loadServlet()方法加载Servlet，
     * loadServlet()由标准Servlet实现类进行实现
     *
     * @return 分配的Servlet 实例
     * @throws ServletException 在加载Servlet实例的过程当中发生错误
     */
    Servlet allocate() throws ServletException;

    /**
     * 加载Servlet实例
     * @return 返回加载的Servlet实例
     * @throws ServletException 在加载Servlet实例的过程当中发生错误
     */
    Servlet loadServlet() throws ServletException;
}
