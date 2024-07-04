package org.ter.lifecycle;

import org.ter.exception.LifecycleException;
import org.ter.ter_server.util.res.StringManager;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class LifecycleBase implements Lifecycle{
    private static final StringManager sm = StringManager.getStringManager(LifecycleBase.class);
    /**
     * 已进行注册的LifecycleListener列表
     */
    private final List<LifecycleListener> lifecycleListeners = new CopyOnWriteArrayList<>();
    /**
     * 组件状态{@link LifecycleState}
     */
    private volatile LifecycleState state = LifecycleState.NEW;
    /**
     * 子类抛出的{@link LifecycleException} 异常，
     * true 继续抛出，供调用方处理
     * false 记录异常
     */
    private boolean throwOnFailure = true;
    public boolean getThrowOnFailure() {
        return throwOnFailure;
    }
    public void setThrowOnFailure(boolean throwOnFailure) {
        this.throwOnFailure = throwOnFailure;
    }

    @Override
    public void addKLifecycleListener(LifecycleListener listener) {
        lifecycleListeners.add(listener);
    }

    @Override
    public LifecycleListener[] findLifecycleListeners() {
        return lifecycleListeners.toArray(new LifecycleListener[0]);
    }

    @Override
    public void remoteLifecycleListener(LifecycleListener listener) {
        lifecycleListeners.remove(listener);
    }

    /**
     * 触发组件的生命周期事件，通知执行生命周期侦听器
     *
     * @param type 事件类型
     * @param data 事件关联数据
     */
    protected void fireLifecycleEvent(String type, Object data){
        LifecycleEvent lifecycleEvent = new LifecycleEvent(this, type, data);
        for (LifecycleListener listener : lifecycleListeners) {
            listener.lifecycleEvent(lifecycleEvent);
        }
    }
    @Override
    public final synchronized void init() throws LifecycleException {
        if(!LifecycleState.NEW.equals(state)){
            invalidTransition(BEFORE_INIT_EVENT);
        }
        try {
            setStateInternal(LifecycleState.INITIALIZING, null, false);
            initInternal();
            setStateInternal(LifecycleState.INITIALIZED, null, false);
        }catch (Throwable throwable) {
            handleSubClassException(throwable, "lifecycleBase.initFail", toString());
        }
    }
    protected abstract void initInternal() throws LifecycleException;
    @Override
    public final synchronized void start() throws LifecycleException {
        if(LifecycleState.STARTING_PREP.equals(state)
                || LifecycleState.STARTING.equals(state)
                || LifecycleState.STARTED.equals(state)){
            return;
        }
        if(LifecycleState.NEW.equals(state)){
            init();
        } else if (LifecycleState.FAILED.equals(state)) {
            stop();
        } else if(!LifecycleState.INITIALIZED.equals(state)
                && !LifecycleState.STOPPED.equals(state)){
            invalidTransition(BEFORE_START_EVENT);
        }
        try {
            setStateInternal(LifecycleState.STARTING_PREP, null, false);
            startInternal();
            if(LifecycleState.FAILED.equals(state)){
                stop();
            }else if(!LifecycleState.STARTING.equals(state)){
                invalidTransition(AFTER_START_EVENT);
            }else{
                setStateInternal(LifecycleState.STARTED, null, false);
            }
        }catch (Throwable throwable){
            handleSubClassException(throwable, "lifecycleBase.startFail", toString());
        }
    }
    protected abstract void startInternal() throws LifecycleException;
    @Override
    public final synchronized void stop() throws LifecycleException {
        if(LifecycleState.STOPPING_PREP.equals(state)
                || LifecycleState.STOPPING.equals(state)
                || LifecycleState.STOPPED.equals(state)){
            return;
        }
        if(LifecycleState.NEW.equals(state)){
            state = LifecycleState.STOPPED;
            return;
        }
        if(!LifecycleState.STARTED.equals(state) && LifecycleState.FAILED.equals(state)){
            invalidTransition(BEFORE_STOP_EVENT);
        }

        try {
            if(LifecycleState.FAILED.equals(state)){
                fireLifecycleEvent(BEFORE_STOP_EVENT, null);
            }else{
                setStateInternal(LifecycleState.STOPPING_PREP, null, false);
            }
            stopInternal();
            if(!LifecycleState.STOPPING.equals(state)
                    && !LifecycleState.FAILED.equals(state)){
                invalidTransition(AFTER_STOP_EVENT);
            }
            setStateInternal(LifecycleState.STOPPED, null,false);
        }catch (Throwable throwable){
            handleSubClassException(throwable, "lifecycleBase", toString());
        }finally {
            if(this instanceof SingleUse){
                setStateInternal(LifecycleState.STOPPED, null,false);
                destroy();
            }
        }
    }

    protected abstract void stopInternal() throws LifecycleException;

    @Override
    public final synchronized void destroy() throws LifecycleException {
        if(LifecycleState.DESTROYING.equals(state)
                || LifecycleState.DESTROYED.equals(state)){
            return;
        }

        if(LifecycleState.FAILED.equals(state)) {
            try {
                stop();
            }catch (LifecycleException exception){
                // 记录日志
            }
        }
        if(!LifecycleState.STOPPED.equals(state)
                && !LifecycleState.FAILED.equals(state)
                && !LifecycleState.NEW.equals(state)
                && LifecycleState.INITIALIZED.equals(state)){
            invalidTransition(BEFORE_DESTROY_EVENT);
        }
        try {
            setStateInternal(LifecycleState.DESTROYING, null, false);
            destroyInternal();
            setStateInternal(LifecycleState.DESTROYED, null, false);
        }catch (Throwable throwable){
            handleSubClassException(throwable, "lifecycleVBase.destroyFail", toString());
        }
    }
    protected abstract void destroyInternal() throws LifecycleException;
    @Override
    public String getStateName() {
        return getLifecycleState().toString();
    }

    @Override
    public LifecycleState getLifecycleState() {
        return state;
    }
    protected synchronized void setLifecycleState(LifecycleState state) throws LifecycleException{
        setStateInternal(state, null,true);
    }
    protected synchronized void setState(LifecycleState state, Object data)
            throws LifecycleException {
        setStateInternal(state, data, true);
    }
    private synchronized void setStateInternal(LifecycleState state, Object data, boolean check) throws LifecycleException{
        if(check){
            if(Objects.isNull(state)){
                invalidTransition("null");
                return;
            }
            if(!(state == LifecycleState.FAILED ||
                    (this.state == LifecycleState.STARTING_PREP && state == LifecycleState.STARTING) ||
                    (this.state == LifecycleState.STOPPING_PREP && state == LifecycleState.STOPPING) ||
                    (this.state == LifecycleState.FAILED && state == LifecycleState.STOPPING))){
                invalidTransition(state.name());
            }
        }
        this.state = state;
        String lifecycleEvent = state.getLifecycleEvent();
        if(Objects.nonNull(lifecycleEvent)){
            fireLifecycleEvent(lifecycleEvent, data);
        }
    }

    private void invalidTransition(String type) throws LifecycleException{
        throw new LifecycleException(sm.getString("lifecycleBase.invalidTransition", type, toString(), state));
    }

    private void handleSubClassException(Throwable throwable, String key, Object... data) throws LifecycleException{
        setStateInternal(LifecycleState.FAILED, null, false);
        String msg = sm.getString(key, data);
        if(getThrowOnFailure()){
            if(!(throwable instanceof LifecycleException)){
                throwable = new LifecycleException(msg, throwable);
            }
            throw (LifecycleException) throwable;
        }else{
            // 记录日志
        }
    }
}
