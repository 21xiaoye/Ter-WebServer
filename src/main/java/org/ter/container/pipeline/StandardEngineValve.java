package org.ter.container.pipeline;

import org.ter.connector.Request;
import org.ter.connector.Response;
import org.ter.container.Host;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class StandardEngineValve extends ValveBase{
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        Host host = request.getHost();
        if(Objects.isNull(host)){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        host.getPipeline().getFirst().invoke(request, response);
    }
}
