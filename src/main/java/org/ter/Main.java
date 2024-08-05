package org.ter;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.ter.config.WebConfig;
import org.ter.container.Context;
import org.ter.controller.HelloServlet;
import org.ter.exception.LifecycleException;
import org.ter.startup.Ter;

import javax.servlet.Servlet;

public class Main {
    public static void main(String[] args) throws LifecycleException{
        Ter ter = new Ter();
        ter.setPort(6771);
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(WebConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);

        Context ctx = ter.addContext("", null);
        ter.addServlet(ctx, "HelloServlet", new HelloServlet());
        ctx.addServletMappingDecoded("/hello", "HelloServlet");
        ter.addServlet(ctx, "dispatcherServlet", (Servlet) dispatcherServlet);
        ctx.addServletMappingDecoded("/", "dispatcherServlet");
        ter.start();
        ter.getServer().await();
    }
}