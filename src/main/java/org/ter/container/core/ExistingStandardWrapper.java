package org.ter.container.core;


import javax.servlet.Servlet;

public class ExistingStandardWrapper extends StandardWrapper{
    private final Servlet existingServlet;
    public ExistingStandardWrapper(Servlet existingServlet){
        this.existingServlet = existingServlet;
    }

    public Servlet getExistingServlet() {
        return existingServlet;
    }

    @Override
    public String getServletName() {
        return existingServlet.getClass().getName();
    }
}
