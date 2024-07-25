package org.ter.coyote;


import java.util.concurrent.Executor;

/**
 * 协议处理器的主要接口，对协议进行处理和管理,协议实现的主要接口。这个接口是对EndPoint和 Processor进一步抽象。<br/>
 * 连接器当中主要由三个组件组成:
 * <li>
 *     <b>EndPoint:</b>{@link org.ter.util.net.AbstractEndpoint}处理Socket，对Socket进行封装{@link org.ter.util.net.wrapper.SocketWrapperBase}交给Processor进行处理，
 * 例如http1.1协议的处理由{@link org.ter.coyote.http1.Http1Processor}处理。
 * </li>
 * <li>
 *     <b>Processor:</b>{@link Processor}，对EndPoint组件传过来的Socket封装器，解析Http，封装成{@link Request}对象，将
 * 该Request对象交给Adapter。
 * </li>
 * <li>
 *     <b>Adapter:</b>{@link Adapter}，处理Processor传过来的{@link Request}对象，并将其封装成ServletRequest对象，将其交给
 * {@link org.ter.container.Container}容器进行处理。
 * </li>
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