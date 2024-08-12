package org.ter.container.pipeline;

import org.ter.connector.Request;
import org.ter.connector.Response;
import org.ter.container.Context;
import org.ter.container.core.StandardWrapper;

import javax.servlet.DispatcherType;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class StandardWrapperValve extends ValveBase{
    @Override
    public void invoke(Request request, Response response) throws IOException {
        StandardWrapper wrapper = (StandardWrapper)getContainer();
        Servlet servlet;
        Context context = (Context) wrapper.getParent();
        if (!context.getLifecycleState().isAvailable()) {
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Servlet [{0}] is currently unavailable");
        }
        try {
            servlet = wrapper.allocate();
            if(Objects.nonNull(servlet)){
                servlet.service(request, response);
            }
        }catch (ServletException exception){
            // 处理错误信息
        }
    }
}
