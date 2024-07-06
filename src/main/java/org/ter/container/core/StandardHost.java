package org.ter.container.core;

import org.ter.container.Container;
import org.ter.container.Host;

public class StandardHost extends ContainerBase implements Host {
    @Override
    public void addChild(Container container) {
        super.addChild(container);
    }
}
