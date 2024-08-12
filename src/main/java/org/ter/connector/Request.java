package org.ter.connector;

import org.ter.container.Context;
import org.ter.container.Host;
import org.ter.container.Wrapper;
import org.ter.coyote.CoyoteRequest;
import org.ter.util.Constants;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

public class Request implements HttpServletRequest {
    private Connector connector;
    private CoyoteRequest coyoteRequest;
    private Response response;
    private String sessionId;
    private InputBuffer inputBuffer = new InputBuffer();
    private final ArrayList<Locale> locales = new ArrayList<>();

    private int remotePort = -1;
    private String remoteAddr;
    private String remoteHost;
    private int localPort = -1;
    private String localName;
    private String localAddr;
    private MappingData mappingData = new MappingData();
    private DispatcherType internalDispatcherType = null;
    private boolean parametersParsed = false;

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public Connector getConnector() {
        return connector;
    }

    public CoyoteRequest getCoyoteRequest() {
        return coyoteRequest;
    }

    public void setCoyoteRequest(CoyoteRequest coyoteRequest) {
        this.coyoteRequest = coyoteRequest;
        inputBuffer.setCoyoteRequest(coyoteRequest);
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public long getDateHeader(String s) {
        return 0;
    }

    @Override
    public String getHeader(String name) {
        return coyoteRequest.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return null;
    }

    @Override
    public int getIntHeader(String name) {
        String header = coyoteRequest.getHeader(name);
        if(Objects.isNull(header)){
            return -1;
        }
        return Integer.parseInt(header);
    }

    @Override
    public String getMethod() {
        return coyoteRequest.getMethodMB();
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        return coyoteRequest.getQueryMB();
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return coyoteRequest.getUriMB();
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean b) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return false;
    }


    @Override
    public void login(String s, String s1) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> httpUpgradeHandlerClass) throws IOException, ServletException {
        return null;
    }


    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return coyoteRequest.getContentLength();
    }

    @Override
    public long getContentLengthLong() {
        return coyoteRequest.getContentLength();
    }

    @Override
    public String getContentType() {
        return coyoteRequest.getContentType();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return coyoteRequest.getProtoMB();
    }

    @Override
    public String getScheme() {
        return coyoteRequest.getSchemeMB();
    }

    @Override
    public String getServerName() {
        return coyoteRequest.getServerNameMB();
    }

    @Override
    public int getServerPort() {
        return coyoteRequest.getServerPort();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        if(Objects.isNull(remoteAddr)){
            remoteAddr =  coyoteRequest.getRemoteAddrMB();
        }
        return remoteAddr;
    }

    @Override
    public String getRemoteHost() {
        if(Objects.isNull(remoteHost)){
            remoteHost = coyoteRequest.getRemoteHostMB();
        }
        return remoteHost;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return locales.get(0);
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return Collections.enumeration(locales);
    }

    @Override
    public boolean isSecure() {
        return Constants.HTTPS.equals(getScheme());
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        if(remotePort == -1){
            remotePort = coyoteRequest.getRemotePort();
        }
        return remotePort;
    }

    @Override
    public String getLocalName() {
        if(Objects.isNull(localName)){
            localName = coyoteRequest.getLocalNameMB();
        }
        return localName;
    }

    @Override
    public String getLocalAddr() {
        if(Objects.isNull(localAddr)){
            localAddr = coyoteRequest.getLocalAddrMB();
        }
        return localAddr;
    }

    @Override
    public int getLocalPort() {
        if(localPort == -1){
            localPort = coyoteRequest.getLocalPort();
        }
        return localPort;
    }

    @Override
    public ServletContext getServletContext() {
        return getContext().getServletContext();
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        if(!isAsyncStarted()){
            throw new IllegalStateException("It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)");
        }
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        if(Objects.isNull(internalDispatcherType)){
            internalDispatcherType = DispatcherType.REQUEST;
        }
        return internalDispatcherType;
    }

    public MappingData getMappingData() {
        return mappingData;
    }
    public Host getHost(){
        return mappingData.host;
    }
    public Context getContext(){
        return mappingData.context;
    }
    public Wrapper getWrapper() {
        return mappingData.wrapper;
    }
}
