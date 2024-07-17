package org.ter;

import org.ter.exception.LifecycleException;
import org.ter.startup.Ter;

public class Main {
    public static void main(String[] args) throws LifecycleException{
        Ter ter = new Ter();
        ter.setPort(6771);
        ter.addContext("/", null);
        ter.start();
        ter.getServer().await();
    }
}