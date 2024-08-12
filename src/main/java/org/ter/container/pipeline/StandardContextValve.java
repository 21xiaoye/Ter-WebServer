package org.ter.container.pipeline;

import org.ter.connector.Request;
import org.ter.connector.Response;
import org.ter.container.Wrapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class StandardContextValve extends ValveBase{
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        Wrapper wrapper = request.getWrapper();
        if(Objects.isNull(wrapper)){
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        wrapper.getPipeline().getFirst().invoke(request, response);
    }
}
