package org.ter.ter_server.util.net;

/**
 * 事件连接状态
 * 定义每个套接字发生的事件，这些事件需要容器进一步处理
 */
public enum SocketEvent {
    /**
     * 可以读取数据
     */
    OPEN_READ,
    /**
     * 可以写入数据
     */
    OPEN_WRITE,
    /**
     * 关联的端口或者连接器正在被关闭，需要关闭套接字
     */
    STOP,
    /**
     * 连接超时，关闭连接
     */
    TIMEOUT,
    /**
     * 客户端断开连接
     */
    DISCONNECT,
    /**
     * 非容器线程上发生错误，处理需要返回到容器进行任何必要的清理。使用此功能的示例包括：
     * by NIO2 表示完成处理程序失败
     * 在 Servlet 3.0 异步处理期间，通过容器在非容器线程上发出 I/O 错误信号。
     */
    ERROR,
    /**
     * 客户端尝试建立连接，连接失败
     */
    CONNECT_FAIL
}
