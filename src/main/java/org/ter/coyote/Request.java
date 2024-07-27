package org.ter.coyote;

import java.nio.charset.Charset;

public final class Request {
    public enum Type{
        METHOD_MB,PROTO_MB,URI_MB,QUERY_MB
    }
    private long startTime = -1;
    private int ServerPort = -1;
    private int localPort = -1;
    private int remotePort = -1;
    private  String serverNameMB;
    private  String schemeMB;
    private  String methodMB;
    private  String uriMB;
    private  String decodedUriMB;
    private  String queryMB;
    private  String protoMB;

    // 远程地址和主机
    private  String remoteAddrMB;
    private  String peerAddrMB;
    private  String localNameMB;
    private  String remoteHostMB;
    private  String localAddrMB;
    private Response response;
    private Charset charset = null;
    private String characterEncoding = null;

    public Request() {
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public int getServerPort() {
        return ServerPort;
    }

    public void setServerPort(int serverPort) {
        ServerPort = serverPort;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getServerNameMB() {
        return serverNameMB;
    }

    public void setServerNameMB(String serverNameMB) {
        this.serverNameMB = serverNameMB;
    }

    public String getSchemeMB() {
        return schemeMB;
    }

    public void setSchemeMB(String schemeMB) {
        this.schemeMB = schemeMB;
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
        return remoteAddrMB;
    }

    public void setRemoteAddrMB(String remoteAddrMB) {
        this.remoteAddrMB = remoteAddrMB;
    }

    public void setPeerAddrMB(String peerAddrMB) {
        this.peerAddrMB = peerAddrMB;
    }

    public void setLocalAddrMB(String localAddrMB) {
        this.localAddrMB = localAddrMB;
    }

    public String getLocalAddrMB() {
        return localAddrMB;
    }

    public void setLocalNameMB(String localNameMB) {
        this.localNameMB = localNameMB;
    }

    public String getLocalNameMB() {
        return localNameMB;
    }

    public void setRemoteHostMB(String remoteHostMB) {
        this.remoteHostMB = remoteHostMB;
    }

    public String getRemoteHostMB() {
        return remoteHostMB;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
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
    public void setStrVal(Type type, byte[] bytes, int start, int end){
        byte[] subArray = new byte[end - start];
        System.arraycopy(bytes, start, subArray, 0, end - start);
        setStrVal(type, subArray);
    }
    public void setStrVal(Type type, byte[] bytes){
        setStrVal(type, new String(bytes));
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
}
