package org.ter.coyote;


import java.util.concurrent.Executor;

/**
 * 协议处理器的主要接口，对协议进行处理和管理，
 * 例如http1.1协议的处理由Http11Protocol处理
 */
public interface ProtocolHandler {
    /**
     * 获取与协议处理器相关的适配器
     *
     * @return 返回与协议处理器相关的适配器
     */
    Adapter getAdapter();

    /**
     * 设置协议处理器的适配器
     *
     * @param adapter 要关联的适配器
     */
    void setAdapter(Adapter adapter);

    /**
     * 获取处理请求的任务执行器
     *
     * @return 返回用于处理请求的任务执行器
     */
    Executor getExecutor();

    /**
     * 初始化协议处理器
     *
     * @throws Exception 协议处理器无法正常初始化
     */
    void init() throws Exception;

    /**
     * 启动协议处理器
     *
     * @throws Exception 协议处理器无法正常启动
     */
    void start() throws Exception;

    /**
     * 暂停协议处理器
     *
     * @throws Exception 协议处理器无法正常暂停
     */
    void pause() throws Exception;

    /**
     * 恢复协议处理器
     *
     * @throws Exception 协议处理器无法正常恢复
     */
    void resume() throws Exception;

    /**
     * 关闭协议处理器
     *
     * @throws Exception 协议处理器无法正常关闭
     */
    void stop() throws Exception;

    /**
     * 销毁协议处理器
     *
     * @throws Exception 协议处理器无法正常销毁
     */
    void destroy() throws Exception;

    /**
     * 如果绑定服务套接字，则关闭服务套接字
     */
    void closeServerSocket();

    /**
     * 等待客户端与服务器的连接正常关闭。
     * 当所有客户端连接都已关闭或该方法一直在等待 waitTimeMillis时，该方法将返回。
     *
     * @param waitMillis 等待客户端连接关闭的最长时间（以毫秒为单位）。
     * @return 返回等待的剩余时间
     */
    long awaitConnectionsClose(long waitMillis);
}
