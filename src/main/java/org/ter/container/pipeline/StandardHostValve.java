package org.ter.container.pipeline;

import org.ter.connector.Request;
import org.ter.connector.Response;
import org.ter.container.Context;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Objects;

public class StandardHostValve extends ValveBase {
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        Context context = request.getContext();
        if(Objects.isNull(context)){
            return;
        }
        context.getPipeline().getFirst().invoke(request,response);
    }
}
