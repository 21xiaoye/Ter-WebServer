package org.ter.container.core;

import org.ter.container.Container;
import org.ter.container.Pipeline;
import org.ter.container.Valve;

public class StandardPipeline extends ContainerBase implements Pipeline {
    public StandardPipeline(Container container){

    }

    @Override
    public Valve getFirst() {
        return null;
    }
}
