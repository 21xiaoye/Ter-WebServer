package org.ter.lifecycle;

import org.ter.container.Container;

import java.util.EventObject;

public class ContainerEvent extends EventObject {
    private static final long serialVersionUID = 1L;
    private final Object data;
    private final String type;

    public ContainerEvent(Container container, String type, Object data) {
        super(container);
        this.type = type;
        this.data = data;
    }
    public String getType() {
        return type;
    }
    public Object getData() {
        return data;
    }
    public Container getContainer() {
        return (Container) super.getSource();
    }
}
