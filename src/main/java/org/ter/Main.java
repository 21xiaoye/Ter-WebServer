package org.ter;

import org.ter.container.Context;
import org.ter.exception.LifecycleException;
import org.ter.startup.Ter;

public class Main {
    public static void main(String[] args) throws LifecycleException{
        Ter ter = new Ter();
        ter.setPort(6771);
        Context context = ter.addContext("", null);
        ter.addServlet(context, "HelloServlet", new HelloServlet());
        context.addServletMappingDecoded("/hello", "HelloServlet");
        ter.start();
        ter.getServer().await();
    }
}