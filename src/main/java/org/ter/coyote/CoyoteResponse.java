package org.ter.coyote;

import org.springframework.cglib.core.Local;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;

public final class CoyoteResponse {
    private static Locale DEFAULT_LOCALE = Locale.getDefault();
    /**
     * 默认的响应状态，表示请求成功
     */
    int status = 200;
    /**
     * 响应信息
     */
    String message = null;
    /**
     * 关联输出缓冲区，将数据输入到Socket
     */
    OutputBuffer outputBuffer;
    CoyoteRequest request;
    /**
     * 响应数据是否已提交的标志
     */
    private boolean committed = false;
    private Locale locale = DEFAULT_LOCALE;
    private volatile ActionHook hook;
    /**
     * 响应正文使用的字符编码
     */
    Charset charset = null;
    //***********************************************************************
    //以下为HTTP协议的一些特定字段
    //***********************************************************************
    /**
     * 用来指明发送给接收者的实体正文的媒体类型，如Content-Type:text/html;charset=GBK
     */
    private String contentType = null;
    /**
     * 描述资源所用的自然语言，与Accept-Language对应,例如zh_cn
     * 此用于描述Response响应所用的自然语言
     */
    private String contentLanguage = null;
    /**
     * 与请求报头Accept-Encoding对应，告诉浏览器服务端采用的是什么压缩编码-一般写全站压缩的时候需要用到的
     */
    private String characterEncoding = null;
    /**
     * 指明实体正文的长度，用以字节方式存储的十进制数字来表示
     */
    long contentLength = -1;

    /**
     * 请求头信息
     */
    private TreeMap<String, String> headersMap = new TreeMap<>();

    public void setOutputBuffer(OutputBuffer outputBuffer) {
        this.outputBuffer = outputBuffer;
    }

    public void setRequest(CoyoteRequest request) {
        this.request = request;
    }
    public CoyoteRequest getRequest() {
        return request;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHook(ActionHook hook) {
        this.hook = hook;
    }

    public void reset(){
        recycle();
    }

    public void recycle() {
        contentType = null;
        contentLanguage = null;
        locale = DEFAULT_LOCALE;
        charset = null;
        characterEncoding = null;
        contentLength = -1;
        status = 200;
        message = null;
        committed = false;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        contentLanguage = locale.toLanguageTag();
    }

    public Locale getLocale() {
        return locale;
    }

    /**
     * 获取自然语言
     * @return 返回此次响应的自然语言
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * 设置此次响应的正文中的字符编码，主要在获取getWrite之前进行调用。
     * 必须设置响应正文所使用的字符编码。
     *
     * @param characterEncoding 新的的自然语言，字符编码的名称
     */
    public void setCharacterEncoding(String characterEncoding) {
        if(isCommitted() || Objects.isNull(characterEncoding)){
            this.characterEncoding = null;
            this.charset = null;
            return;
        }
        this.characterEncoding = characterEncoding;
        this.charset = Charset.forName(characterEncoding);
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentLanguage() {
        return contentLanguage;
    }

    public boolean isCommitted() {
        return committed;
    }
    public void setCommitted(boolean committed) {
        this.committed = committed;
    }
    public void setHeader(String key, String value){
        if(headersMap.containsKey(key)){
            headersMap.remove(key);
        }
        headersMap.put(key, value);
    }
    public void addHeader(String key, String value) {
        headersMap.put(key, value);
    }
    public String getHeader(String name){
        String headerValue = headersMap.get(name);
        if(Objects.isNull(headerValue)){
            return null;
        }
        return headerValue;
    }
    public TreeMap<String, String> getHeadersMap() {
        return headersMap;
    }
    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }
    public long getContentLengthLong() {
        return contentLength;
    }

    /**
     * 发送此次请求的响应标头数据，然后发送此次请求的响应正文数据
     */
    public void sendHeaders(){
        action(ActionCode.COMMIT, this);
        setCommitted(true);
    }
    private void action(ActionCode actionCode, Object params){
        if(Objects.nonNull(hook)){
            if(Objects.isNull(params)){
                hook.action(actionCode, this);
            }else{
                hook.action(actionCode, params);
            }
        }
    }
    public void doWrite(ByteBuffer chunk) throws IOException {
        outputBuffer.doWrite(chunk);
    }
    public String getFieldValue(String name) {
        String value = headersMap.get(name);
        if(value  == null){
            return "";
        }
        return value;
    }
}
