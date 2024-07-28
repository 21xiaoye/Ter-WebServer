package org.ter.coyote;

import java.nio.charset.Charset;

public final class CoyoteResponse {
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
     * 响应正文使用的字符编码
     */
    Charset charset = null;
    //***********************************************************************
    //以下为HTTP协议的一些特定字段
    //***********************************************************************
    /**
     * 用来指明发送给接收者的实体正文的媒体类型，如Content-Type:text/html;charset=GBK
     */
    String contentType = null;
    /**
     * 描述资源所用的自然语言，与Accept-Language对应,例如zh_cn
     * 此用于描述Response响应所用的自然语言
     */
    String contentLanguage = null;
    /**
     * 与请求报头Accept-Encoding对应，告诉浏览器服务端采用的是什么压缩编码-一般写全站压缩的时候需要用到的
     */
    String characterEncoding = null;
    /**
     * 指明实体正文的长度，用以字节方式存储的十进制数字来表示
     */
    long contentLength = -1;

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
        this.characterEncoding = characterEncoding;
    }
}
