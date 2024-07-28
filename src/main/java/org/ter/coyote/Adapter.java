package org.ter.coyote;

/**
 * 适配器，主要作用是将原始的http请求转发为Servlet中
 * 标准的HttpServletRequest对象，是基于coyote中的
 * servlet的入口点
 */
public interface Adapter {
    /**
     * 调用服务方法处理请求，将请求交给容器进行处理
     *
     * @param request 请求对象
     * @param response 响应对象
     * @exception Exception 处理请求期间发生错误
     */
    void service(CoyoteRequest request, CoyoteResponse response) throws Exception;
}
