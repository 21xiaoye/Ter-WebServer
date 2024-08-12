package org.ter.container.pipeline;

import org.ter.connector.Request;
import org.ter.connector.Response;

import javax.servlet.ServletException;
import java.io.IOException;

public class ErrorReportValve extends ValveBase{
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        getNext().invoke(request, response);
    }
}
