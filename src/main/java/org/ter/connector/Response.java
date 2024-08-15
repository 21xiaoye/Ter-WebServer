package org.ter.connector;

import org.ter.coyote.CoyoteResponse;
import org.ter.coyote.OutputBuffer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Response implements HttpServletResponse {
    private CoyoteResponse coyoteResponse;
    private Request request;
    private PrintWriter printWriter;
    private OutBuffer outBuffer;
    private ServletOutputStream outputStream;
    public Response(){
        outBuffer = new OutBuffer();
        printWriter = new PrintWriter(outBuffer);
    }
    public CoyoteResponse getCoyoteResponse() {
        return coyoteResponse;
    }
    public Request getRequest() {
        return request;
    }
    public void setRequest(Request request) {
        this.request = request;
    }
    public void setCoyoteResponse(CoyoteResponse coyoteResponse) {
        this.coyoteResponse = coyoteResponse;
        outBuffer.setCoyoteResponse(coyoteResponse);
    }
    @Override
    public void addCookie(Cookie cookie) {

    }
    @Override
    public boolean containsHeader(String name) {
        return coyoteResponse.getHeadersMap().containsKey(name);
    }
    @Override
    public String encodeURL(String url) {
        return url;
    }
    @Override
    public String encodeRedirectURL(String url) {
        return url;
    }
    @Override
    public String encodeUrl(String url) {
        return encodeURL(url);
    }
    @Override
    public String encodeRedirectUrl(String url) {
        return encodeRedirectURL(url);
    }
    @Override
    public void sendError(int status, String message) throws IOException {

    }
    @Override
    public void sendError(int status) throws IOException {
        sendError(status, null);
    }
    @Override
    public void sendRedirect(String s) throws IOException {

    }
    @Override
    public void setDateHeader(String name, long value) {
        if(Objects.isNull(name) || isCommitted()){
            return;
        }
        coyoteResponse.setHeader(name, String.valueOf(value));
    }

    @Override
    public void addDateHeader(String name, long value) {
        if(isCommitted() || Objects.isNull(name)){
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        String format = simpleDateFormat.format(new Date(value));
        addHeader(name, format);
    }

    @Override
    public void setHeader(String key, String value) {
        getCoyoteResponse().setHeader(key, value);
    }

    @Override
    public void addHeader(String key, String value) {
        getCoyoteResponse().addHeader(key, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        if(Objects.isNull(name) || isCommitted()){
            return;
        }
        setHeader(name, ""+value);
    }

    @Override
    public void addIntHeader(String name, int value) {
        if(Objects.isNull(name) || isCommitted()){
            return;
        }
        addHeader(name, ""+value);
    }

    @Override
    public void setStatus(int status) {
        setStatus(status, null);
    }

    @Override
    public void setStatus(int status, String message) {
        if(isCommitted()){
            return;
        }
        coyoteResponse.setStatus(status);
        coyoteResponse.setMessage(message);
    }

    @Override
    public int getStatus() {
        return coyoteResponse.getStatus();
    }

    @Override
    public String getHeader(String name) {
        return coyoteResponse.getHeader(name);
    }

    @Override
    public Collection<String> getHeaders(String s) {
        return coyoteResponse.getHeadersMap().values();
    }

    @Override
    public Collection<String> getHeaderNames() {
        return coyoteResponse.getHeadersMap().keySet();
    }

    @Override
    public String getCharacterEncoding() {
        return coyoteResponse.getCharacterEncoding();
    }
    @Override
    public String getContentType() {
        return coyoteResponse.getContentType();
    }
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if(Objects.isNull(outputStream)){
            outputStream = new CoyoteOutputStream();
        }
        return outputStream;
    }
    @Override
    public PrintWriter getWriter() throws IOException {
        if(Objects.isNull(printWriter)){
            printWriter = new PrintWriter(outBuffer);
        }
        return printWriter;
    }
    @Override
    public void setCharacterEncoding(String characterEncoding) {
        if(isCommitted()){
            return;
        }
        coyoteResponse.setCharacterEncoding(characterEncoding);
    }
    @Override
    public void setContentLength(int length) {
        setContentLengthLong(length);
    }
    @Override
    public void setContentLengthLong(long length) {
        if(isCommitted()){
            return;
        }
        getCoyoteResponse().setContentLength(length);
    }
    @Override
    public void setContentType(String contentType) {
        if(isCommitted()){
            return;
        }
        coyoteResponse.setContentType(contentType);
    }
    @Override
    public void setBufferSize(int bufferSize) {
        if(isCommitted()){
            throw new IllegalStateException("Cannot change buffer size after data has been written");
        }
        outBuffer.setBufferSize(bufferSize);
    }
    @Override
    public int getBufferSize() {
        return outBuffer.getBufferSize();
    }
    @Override
    public void flushBuffer() throws IOException {
        outBuffer.flush();
    }
    @Override
    public void resetBuffer() {
        outBuffer.reset();
    }
    @Override
    public boolean isCommitted() {
        return coyoteResponse.isCommitted();
    }
    @Override
    public void reset() {
        getCoyoteResponse().reset();
        outBuffer.reset();
    }
    @Override
    public void setLocale(Locale locale) {
        coyoteResponse.setLocale(locale);
    }
    @Override
    public Locale getLocale() {
        return coyoteResponse.getLocale();
    }

    /**
     * 关闭和刷新输出流缓冲区
     * @throws IOException 在此过程当中发生I/O错误
     */
    public void finishResponse() throws IOException {
        outBuffer.close();
    }
}
