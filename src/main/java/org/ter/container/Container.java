package org.ter.container;

import org.ter.lifecycle.ContainerListener;
import org.ter.lifecycle.Lifecycle;

import java.beans.PropertyChangeListener;
public interface Container extends Lifecycle {
    /**
     * 添加子容器时发送 addChild()的
     * ContainerEvent 事件类型
     */
    String ADD_CHILD_EVENT = "addChild";
    /**
     * 添加阀门 addValve()时发送的
     * ContainerEvent事件类型，如果此容器支持管道。
     */
    String ADD_VALVE_EVENT = "addValue";
    /**
     * 删除子容器时发送 removeChild()的
     * ContainerEvent 事件类型
     */
    String REMOVE_CHILD_EVENT = "removeChild";
    /**
     * 删除阀门 removeValve()时发送的
     * ContainerEvent事件类型，如果此容器支持管道。
     */
    String REMOVE_VALUE_EVENt = "removeValue";

    /**
     * 获取当前容器名称
     *
     * @return  当前容器的名称
     */
    String getName();

    /**
     * 设置容器名称，名称必须唯一
     *
     * @param name  设置的当前容器名称
     */
    void setName(String name);

    /**
     * 获取当前容器父容器
     *
     * @return  容器的父容器
     */
    Container getParent();

    /**
     * 设置当前容器的父容器
     *
     * @param container 将此容器作为子容器添加到的容器
     */
    void setParent(Container container);

    /**
     * 获取父类加载器
     *
     * @return 获取当前组件的的父类加载器
     */
    ClassLoader getParentClassLoader();

    /**
     * 设置当前组件的父类加载器
     *
     * @param parentClassLoader 新的父类加载器
     */
    void setParentClassLoader(ClassLoader parentClassLoader);

    /**
     * 当前容器添加子容器
     *
     * @param container 需要在当前容器添加的子容器
     */
    void addChild(Container container);

    /**
     * 当前组件的容器添加容器事件侦听器
     *
     * @param listener  当前组件的容器事件侦听器
     */
    void addContainerListener(ContainerListener listener);

    /**
     * 添加属性更改侦听器
     *
     * @param listener  要添加的属性侦听器
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * 根据名称获取子容器
     *
     * @param name  子容器的名称
     * @return  返回给定名称的子容器
     */
    Container findChild(String name);

    /**
     * 获取与此容器相关联的子容器
     *
     * @return  与此容器相关联的子容器，没有返回空数组
     */
    Container[] findChildren();

    /**
     * 获取与此容器像关联容器侦听器
     *
     * @return  与此容器相关联的容器事件侦听器
     */
    ContainerListener[] findContainerListeners();

    /**
     * 删除与此容器相关联的子容器
     *
     * @param container 需要删除的子容器
     */
    void removeChild(Container container);

    /**
     * 删除与此容器相关联的容器事件侦听器
     *
     * @param listener  需要删除的事件侦听器
     */
    void removeContainerListener(ContainerListener listener);

    /**
     * 删除于此容器相关联的属性事件侦听器
     *
     * @param listener  需要删除的属性事件侦听器
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     *  通知与此容器相关联的容器事件侦听器，此容器发生了事件
     *
     * @param type  事件类型
     * @param data  事件数据
     */
    void fireContainerEvent(String type, Object data);

    /**
     *返回 Pipeline 对象，该对象管理与此容器关联的 Valve
     *
     * @return 管道
     */
    Pipeline getPipeline();
}
