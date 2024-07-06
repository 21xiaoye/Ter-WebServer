package org.ter;

import org.ter.exception.LifecycleException;
import org.ter.startup.Ter;

public class Main {
    public static void main(String[] args) throws LifecycleException{
        Ter ter = new Ter();
        ter.start();
        ter.getServer().await();
        System.out.println("Hello world!");
    }
}