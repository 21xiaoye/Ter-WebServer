package org.ter.container;

import jakarta.servlet.ServletContext;

public interface Context extends Container{
    /**
     * 获取上下文文档根目录
     *
     * @return 山下问文档根目录
     */
    String getDocBase();

    /**
     * 设置上下文文档根目录
     *
     * @param docBase  新的上下文文档根目录
     */
    void setDocBase(String docBase);
    /**
     * 获取URL编码的上下文路径
     *
     * @return 返回URL编码的上下文路径
     */
    String getEncodedPath();

    /**
     * 获取当前Web应用程序的上下文路径
     *
     * @return 当前Web应用程序的上下文路径
     */
    String getPath();

    /**
     * 设置当前应用程序的上下文路径
     *
     * @param path 新的Web应用程序的上下文路径
     */
    void setPath(String path);

    /**
     * 获取servlet上下文
     * @return 返回此上下文的Servlet Context
     */
    ServletContext getServletContext();































}
