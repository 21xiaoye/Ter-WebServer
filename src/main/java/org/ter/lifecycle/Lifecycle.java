package org.ter.lifecycle;

import org.ter.exception.LifecycleException;

public interface Lifecycle extends SingleUse{
    String BEFORE_INIT_EVENT = "before_init";
    String AFTER_INIT_EVENT = "after_init";
    String START_EVENT = "start";
    String BEFORE_START_EVENT = "before_start";
    String AFTER_START_EVENT = "after_start";

    String STOP_EVENT = "stop";
    String BEFORE_STOP_EVENT = "before_stop";
    String AFTER_STOP_EVENT = "after_stop";

    String AFTER_DESTROY_EVENT = "after_destroy";
    String BEFORE_DESTROY_EVENT = "before_destroy";
    String PERIODIC_EVENT = "periodic";
    String CONFIGURE_START_EVENT = "configure_start";
    String CONFIGURE_STOP_EVENT = "configure_stop";

    /**
     * 添加LifecycleEvent到当前组件
     *
     * @param listener 需要添加的LifecycleEvent
     */
    void addLifecycleListener(LifecycleListener listener);

    /**
     * 获取与当前生命周期相关联的LifecycleEvent
     *
     * @return 获取与当前生命周期相关联的LifecycleEvent，组件未注册返回空数组
     */
    LifecycleListener[] findLifecycleListeners();

    /**
     * 删除当前组件的LifecycleEvent
     *
     * @param listener 需要删除的LifecycleEvent
     */
    void remoteLifecycleListener(LifecycleListener listener);

    /**
     * 准备组件初始化，在组件进行对象创建后进行自定义初始化
     *
     * @throws LifecycleException 发生异常错误时，抛出错误
     */
    void init() throws LifecycleException;

    /**
     * 启动组件
     *
     * @throws LifecycleException 发生异常错误时，抛出错误
     */
    void start() throws LifecycleException;

    /**
     * 终止此组件，释放资源
     *
     * @throws LifecycleException 发生异常错误时，抛出错误
     */
    void stop() throws LifecycleException;

    /**
     * 进行组件销毁操作，清理所有资源
     *
     * @throws LifecycleException 发生异常错误时，抛出错误
     */
    void destroy() throws LifecycleException;

    /**
     * 获取组件当前状态
     *
     * @return  组件当前状态
     */
    LifecycleState getLifecycleState();

    /**
     * 获取当前组件状态的文本形式
     *
     * @return  组件当前状态的文本形式
     */
    String getStateName();
}
