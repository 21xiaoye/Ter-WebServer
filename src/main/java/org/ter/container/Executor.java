package org.ter.container;

import org.ter.lifecycle.Lifecycle;

import java.util.concurrent.TimeUnit;


public interface Executor extends Lifecycle,java.util.concurrent.Executor {
    String getName();

    @Deprecated
    void execute(Runnable command, long timeOut, TimeUnit timeUnit);
}
