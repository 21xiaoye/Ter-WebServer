package org.ter.lifecycle;

/**
 * 生命周期状态侦听器，当组件的状态被更改后，触发侦听器
 */
public interface LifecycleListener {
    void lifecycleEvent(LifecycleEvent event);
}
