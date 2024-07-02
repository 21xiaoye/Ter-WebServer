package org.ter.lifecycle;

import java.util.EventObject;

public final class LifecycleEvent extends EventObject {
    private static final long serialVersionUID = 1L;

    private final Object data;
    private final String type;

    /**
     * Constructs a prototypical Event.
     */
    public LifecycleEvent(Lifecycle lifecycle, String type, Object data) {
        super(lifecycle);
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }
    public Object getData() {
        return data;
    }
    public Lifecycle getLifecycle() {
        return (Lifecycle) super.getSource();
    }
}
