package org.ter.container;

/**
 * 解耦接口，指定一个实现类最多与一个容器实例相关联
 */
public interface Contained {
    /**
     * 获取与当前实例相关联的Container对象
     *
     * @return 返回当前实例关联的Container对象。如果没有关联的Container，则返回null
     */
    Container getContainer();

    /**
     * 设置当前实例关联的Container对象。
     * 允许在运行时更改组件所属的Container
     *
     * @param container 要与此实例关联的容器实例
     */
    void setContainer(Container container);
}
