package org.ter.coyote;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 从SocketChannel中读取数据，读取数据的操作在Adapter中进行
 */
public interface InputBuffer {
    /**
     * 从关联的SocketWrapper当中读取数据到缓冲区
     *
     * @return true有数据被读取，false没有读取到数据
     * @throws IOException 在读取的过程当中发生错误
     */
    int read(ByteBuffer byteBuffer) throws IOException;
}
