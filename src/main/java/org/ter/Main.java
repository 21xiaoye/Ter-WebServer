package org.ter;

import org.ter.container.core.StandardServer;

public class Main {
    public static void main(String[] args) {
        StandardServer standardServer = new StandardServer();
        standardServer.await();
        System.out.println("Hello world!");
    }
}