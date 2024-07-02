package org.ter.lifecycle;

/**
 * 监听容器，当容器内部发生动态变化时，会触发侦听器
 * 例如添加添加、删除子容器等操作
 */
public interface ContainerListener {
    void containerEvent(ContainerEvent event);
}
