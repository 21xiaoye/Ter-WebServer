package org.ter.coyote.http11;

import org.ter.coyote.AbstractProcessor;
import org.ter.coyote.HttpParse;
import org.ter.coyote.SocketState;
import org.ter.util.Constants;
import org.ter.util.net.AbstractEndpoint;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;


public class Http11Processor extends AbstractProcessor {
    private final AbstractHttp11Protocol<?> protocol;
    protected Http11InputBuffer inputBuffer;
    private Http11OutputBuffer outputBuffer;
    /**
     * 这个用来是否是长连接的标识，用来管理HTTP请求持久化。
     * true表示完成一次请求之后，不会中断连接，而是继续等待后续请求并处理。
     */
    protected volatile boolean keepAlive = true;
    public Http11Processor(AbstractHttp11Protocol<?> protocol, AbstractEndpoint<?, ?> endpoint) {
        super(endpoint);
        this.protocol = protocol;
        this.inputBuffer = new Http11InputBuffer(request, protocol.getMaxHttpHeaderSize());
        this.outputBuffer = new Http11OutputBuffer(response,protocol.getMaxHttpResponseHeaderSize());
    }

    @Override
    protected SocketState service(SocketWrapperBase<?> socketWrapper) throws IOException {
        setSocketWrapper(socketWrapper);
        while (keepAlive && !endpoint.isPaused()){
            try {
                // 解析 HTTP 请求行
                if (!inputBuffer.parseRequestLineSplit()) {
                    return SocketState.UPGRADING;
                }
                if(endpoint.isPaused()){
                    response.setStatus(503);
                }else{
                    // 解析 HTTP 请求头
                    if(!inputBuffer.parseHeaders()){
                        break;
                    }
                    verifySettingRequest();
                }
                getAdapter().service(request,response);
                keepAlive = false;
            }catch (Throwable throwable){
                response.setStatus(400);
            }
        }
        String httpResponse = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: 13\r\n" +
                "\r\n" +
                "Hello, world!";
        ByteBuffer wrap = ByteBuffer.wrap(httpResponse.getBytes());
        socketWrapper.write(wrap);
        return SocketState.CLOSED;
    }

    /**
     * 这个方法主要是设置和验证请求头信息
     * 需在{@link Http11InputBuffer#parseHeaders()}解析HTTP请求头之后执行
     */
    private void verifySettingRequest(){
        request.setSchemeMB(endpoint.isSSLEnabled() ? "https" : "http");

        String connectionMB = request.getHeader(Constants.CONNECTION);
        if(HttpParse.containsFieldValue(connectionMB, Constants.CLOSE)){
            keepAlive = false;
        }else if(HttpParse.containsFieldValue(connectionMB, Constants.KEEP_ALIVE_HEADER_VALUE_TOKEN)){
            keepAlive = true;
        }

        String hostMB = request.getHeader(Constants.HOST);
        if(Objects.isNull(hostMB)){
            response.setStatus(400);
            request.setServerNameMB("");
            request.setServerPort(socketWrapper.getLocalPort());
        }else {
            String[] hostSplit = hostMB.split(Constants.COLON_STR);
            if (hostSplit.length == 1) {
                request.setServerNameMB(hostSplit[0]);
            }
            request.setServerNameMB(hostSplit[0]);
            request.setServerPort(Integer.parseInt(hostSplit[1]));
        }
    }
    @Override
    protected void setSocketWrapper(SocketWrapperBase<?> socketWrapper) {
        super.setSocketWrapper(socketWrapper);
        request.setSocketWrapper(socketWrapper);
        inputBuffer.init(socketWrapper);
        outputBuffer.init(socketWrapper);
    }
}
