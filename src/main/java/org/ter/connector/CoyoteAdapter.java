package org.ter.connector;

import org.ter.container.Valve;
import org.ter.coyote.Adapter;
import org.ter.coyote.CoyoteRequest;
import org.ter.coyote.CoyoteResponse;
import org.ter.util.Constants;

import javax.servlet.SessionTrackingMode;
import java.io.IOException;
import java.util.Objects;

/**
 * 适配器，将Processor传过来的CoyoteRequest对象转换成HttpServletRequest对象
 * 将其交给Container容器进行处理。<br/>
 * 在这里主要是调用关联的Connector进行处理，在Connector会调用相应的Service。
 * 然后经过管道{@link org.ter.container.Pipeline},在管道中有一系列的
 * {@link Valve}的阀门进行处理
 */
public class CoyoteAdapter implements Adapter {
    private Connector connector;
    public CoyoteAdapter(Connector connector){
        this.connector = connector;
    }
    @Override
    public void service(CoyoteRequest coyoteRequest, CoyoteResponse coyoteResponse) throws Exception {
        Request request = connector.createRequest(coyoteRequest);
        Response response = connector.createResponse(coyoteResponse);
        request.setResponse(response);
        response.setRequest(request);
        boolean postParseRequest = postParseRequest(request, coyoteRequest, response, coyoteResponse);
        if(postParseRequest){
            connector.getService().getContainer().getPipeline().getFirst().invoke(request, response);
        }
        // Servlet执行完请求，进行响应返回和关闭资源等操作
        response.finishResponse();
    }

    private boolean postParseRequest(Request request, CoyoteRequest coyoteRequest, Response response, CoyoteResponse coyoteResponse) throws IOException {
        int serverPort = coyoteRequest.getServerPort();
        // 设置默认端口
        if(serverPort == -1){
            if(Constants.HTTPS.equals(coyoteRequest.getSchemeMB())){
                coyoteRequest.setServerPort(443);
            }else{
                coyoteRequest.setServerPort(80);
            }
        }
        // 处理预检请求
        String uriMB = coyoteRequest.getUriMB();
        if(Constants.ALL.equals(uriMB)){
            if(Constants.OPTIONS.equals(coyoteRequest.getMethodMB())){
                response.setHeader("Allow", "GET, HEAD, POST, PUT, DELETE, OPTIONS");
                return false;
            }
        }
        coyoteRequest.setDecodedUriMB(uriMB);
        boolean mapRequired = true;
        while (mapRequired){
            connector.getService().getMapper().map(coyoteRequest.getServerNameMB(), coyoteRequest.getDecodedUriMB(), request.getMappingData());
            if(Objects.isNull(request.getContext())){
                return true;
            }
            mapRequired = false;
        }
        return true;
    }
}
