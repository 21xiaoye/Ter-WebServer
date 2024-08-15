package org.ter.coyote;

import org.ter.util.Constants;
import org.ter.util.net.wrapper.SocketWrapperBase;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class CoyoteRequest {
    public enum Type{
        METHOD_MB,PROTO_MB,URI_MB,QUERY_MB
    }
    private long startTime = -1;
    private int serverPort = -1;
    private int localPort = -1;
    private int remotePort = -1;
    private  String serverNameMB;
    /**
     * HTTP 请求类型 GET、POST......
     */
    private  String methodMB;
    /**
     * HTTP 请求路径
     */
    private  String uriMB;
    private String schemeMB;
    private  String decodedUriMB;
    /**
     * HTTP 请求参数
     */
    private  String queryMB;
    /**
     * HTTP 请求协议
     */
    private  String protoMB;

    // 远程地址和主机
    private  String remoteAddrMB;
    private  String peerAddrMB;
    private  String localNameMB;
    private  String remoteHostMB;
    private  String localAddrMB;
    private CoyoteResponse response;
    private Charset charset = null;
    private String characterEncoding = null;
    private SocketWrapperBase<?> socketWrapper;
    /**
     * HTTP 请求头信息
     */
    private TreeMap<String, String> headersMap;
    private final Map<String, ArrayList<String>> paramHashValues = new LinkedHashMap<>();
    private int contentLength = -1;
    private String contentType = null;
    private InputBuffer inputBuffer;

    public CoyoteRequest() {
        headersMap = new TreeMap<>();
    }

    public long getStartTime() {
        return startTime;
    }

    public void setInputBuffer(InputBuffer inputBuffer) {
        this.inputBuffer = inputBuffer;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getLocalPort() {
        if(localPort == -1){
            localPort = socketWrapper.getLocalPort();
        }
        return localPort;
    }
    public int getRemotePort() {
        if(remotePort == -1){
            remotePort = socketWrapper.getRemotePort();
        }
        return remotePort;
    }
    public String getServerNameMB() {
        return serverNameMB;
    }
    public void setServerNameMB(String serverNameMB) {
        this.serverNameMB = serverNameMB;
    }
    public String getMethodMB() {
        return methodMB;
    }
    public void setMethodMB(String methodMB) {
        this.methodMB = methodMB;
    }
    public String getUriMB() {
        return uriMB;
    }
    public void setUriMB(String uriMB) {
        this.uriMB = uriMB;
    }
    public String getSchemeMB() {
        return schemeMB;
    }
    public void setSchemeMB(String schemeMB) {
        this.schemeMB = schemeMB;
    }
    public String getDecodedUriMB() {
        return decodedUriMB;
    }
    public void setDecodedUriMB(String decodedUriMB) {
        this.decodedUriMB = decodedUriMB;
    }
    public void setQueryMB(String queryMB) {
        this.queryMB = queryMB;
    }
    public String getQueryMB() {
        return queryMB;
    }
    public void setProtoMB(String protoMB) {
        this.protoMB = protoMB;
    }
    public String getProtoMB() {
        return protoMB;
    }
    public String getPeerAddrMB() {
        return peerAddrMB;
    }
    public String getRemoteAddrMB() {
        if(Objects.isNull(remoteAddrMB)){
            remoteAddrMB = socketWrapper.getRemoteAddr();
        }
        return remoteAddrMB;
    }
    public String getLocalAddrMB() {
        if(Objects.isNull(localAddrMB)){
            localAddrMB = socketWrapper.getLocalAddr();
        }
        return localAddrMB;
    }
    public String getLocalNameMB() {
        if(Objects.isNull(localNameMB)){
            localNameMB = socketWrapper.getLocalName();
        }
        return localNameMB;
    }
    public String getRemoteHostMB() {
        if(Objects.isNull(remoteHostMB)){
            remoteHostMB = socketWrapper.getRemoteName();
        }
        return remoteHostMB;
    }
    public CoyoteResponse getResponse() {
        return response;
    }
    public void setResponse(CoyoteResponse response) {
        this.response = response;
        response.setRequest(this);
    }
    public Charset getCharset() {
        return charset;
    }
    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }
    public String getCharacterEncoding() {
        return characterEncoding;
    }
    public void setCharset(Charset charset) {
        this.charset = charset;
    }
    public void setSocketWrapper(SocketWrapperBase<?> socketWrapper) {
        this.socketWrapper = socketWrapper;
    }
    public int getContentLength() {
        if(contentLength > -1){
            return contentLength;
        }
        String headerValue = getHeader(Constants.CONTENT_LENGTH);
        contentLength = Integer.parseInt(headerValue);
        return contentLength;
    }
    public String getContentType() {
        if(Objects.isNull(contentType)){
            contentType = getHeader(Constants.CONTENT_TYPE);
        }
        return contentType;
    }
    public void setStrVal(Type type, byte[] bytes, int start, int end){
        setStrVal(type, new String(bytes, start, end, StandardCharsets.US_ASCII));
    }
    public void setStrVal(Type type, byte[] bytes){
        setStrVal(type, new String(bytes, StandardCharsets.US_ASCII));
    }
    public void setStrVal(Type type,String strVal){
        switch (type){
            case METHOD_MB -> {
                setMethodMB(strVal);
                break;
            }
            case PROTO_MB -> {
                setProtoMB(strVal);
                break;
            }
            case URI_MB -> {
                setUriMB(strVal);
                break;
            }
            case QUERY_MB -> {
                setQueryMB(strVal);
                break;
            }
            default -> {
                // 抛出异常
            }
        }
    }
    public void putHeader(String name, String value){
        headersMap.put(name, value);
    }
    public String getHeader(String name){
        String headerValue = headersMap.get(name);
        if(Objects.isNull(headerValue)){
            return null;
        }
        return headerValue;
    }
    public boolean hasFieldValue(String name) {
        return headersMap.containsKey(name);
    }
    public String getFieldValue(String name) {
        String value = headersMap.get(name);
        if(value  == null){
            return "";
        }
        return value;
    }
    public Enumeration<String> getParameterNames() {
        if (Objects.isNull(queryMB)) {
            return Collections.enumeration(paramHashValues.keySet());
        }
        handleQueryParams();
        return Collections.enumeration(paramHashValues.keySet());
    }

    public String[] getParamHashValues(String name) {
        handleQueryParams();
        ArrayList<String> values = paramHashValues.get(name);
        if (values == null) {
            return null;
        }
        return values.toArray(new String[0]);
    }
    private void handleQueryParams(){
        byte[] queryBytes = queryMB.getBytes();
        int len = queryBytes.length;
        int start = 0;

        while (start < len) {
            // 找到 '=' 的位置
            int equalPos = -1;
            for (int i = start; i < len; i++) {
                if (queryBytes[i] == Constants.EQUAL) {
                    equalPos = i;
                    break;
                }
            }
            // 找到 '&' 的位置
            int andPos = -1;
            for (int i = start; i < len; i++) {
                if (queryBytes[i] == Constants.AND) {
                    andPos = i;
                    break;
                }
            }
            if (equalPos != -1) {
                String key = new String(queryBytes, start, equalPos - start);
                String value;

                if (andPos == -1) {
                    // 没有 '&'，意味着这是最后一个参数
                    value = new String(queryBytes, equalPos + 1, len - equalPos - 1);
                    start = len;
                } else {
                    value = new String(queryBytes, equalPos + 1, andPos - equalPos - 1);
                    start = andPos + 1;
                }

                // 将键和值放入 paramHashValues 中
                paramHashValues.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            } else {
                // 如果找不到 '='，跳过该片段
                start = andPos != -1 ? andPos + 1 : len;
            }
        }
    }
}
