package org.ter.coyote;

/**
 * 连接状态
 */
public enum SocketState {
    OPEN, CLOSED, LONG, ASYNC_END, SENDFILE, UPGRADING, UPGRADED, SUSPENDED
}
