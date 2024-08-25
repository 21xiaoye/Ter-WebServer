package org.ter.controller;


import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.getWriter().println("<h1>Hello, World!--->Get</h1>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int contentLength = req.getContentLength();
        ServletInputStream inputStream = req.getInputStream();
        byte[] buffer = new byte[contentLength];
        inputStream.read(buffer, 0, contentLength);
        System.out.println(new String(buffer));
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()){
            String parameterName = parameterNames.nextElement();
            String parameter = req.getParameter(parameterName);
            System.out.println("参数值为"+parameterName+":"+parameter);
        }
        resp.setContentType("text/html");
        resp.getWriter().println("<h1>Hello, World!--->Post</h1>");
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.getWriter().println("<h1>Hello, World!--->Put</h1>");
    }
}
