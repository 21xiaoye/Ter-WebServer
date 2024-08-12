package org.ter.container.core;


import javax.servlet.Servlet;
import javax.servlet.ServletException;

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

    @Override
    public synchronized Servlet loadServlet() throws ServletException {
        Servlet servlet;
        try {
            servlet = existingServlet.getClass().getConstructor().newInstance();
        }catch (ReflectiveOperationException exception){
            throw new ServletException(exception);
        }
        return servlet;
    }
}
