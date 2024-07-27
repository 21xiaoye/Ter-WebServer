package org.ter.coyote;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 输出缓冲区。
 * 此类由协议实现在内部使用。通过 Response.doWrite（） 进行。
 */
public interface OutputBuffer {
    /**
     * 将给定数据写入响应。
     *
     * @param buffer 需要写入的数据
     * @return 写入的字节数
     * @throws IOException 在写入到响应的过程当中发生错误
     */
    int doWrite(ByteBuffer buffer) throws IOException;
}
