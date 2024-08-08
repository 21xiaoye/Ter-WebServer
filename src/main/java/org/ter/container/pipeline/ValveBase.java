package org.ter.container.pipeline;

import org.ter.container.Contained;
import org.ter.container.Container;
import org.ter.container.Valve;
import org.ter.exception.LifecycleException;


public abstract class ValveBase implements Contained, Valve {
    private Container container;
    private Valve next;
    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public Valve getNext() {
        return next;
    }

    @Override
    public void setNext(Valve valve) {
        this.next = valve;
    }
}
