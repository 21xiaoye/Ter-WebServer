package org.ter.container.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.channels.SocketChannel;

/**
 * EndPoint使用的 SocketChannel 包装器的基类。
 */
public class NioChannel implements ByteChannel, ScatteringByteChannel, GatheringByteChannel {
    protected final SocketBufferHandler bufferHandler;
    protected SocketChannel socketChannel = null;

    public NioChannel(SocketBufferHandler bufferHandler){
        this.bufferHandler = bufferHandler;
    }

    /**
     * 从指定的缓冲区向此通道写入数据
     *
     * @param src
     *         The buffer from which bytes are to be retrieved
     *
     * @return  写入的字节数
     * @throws IOException 写入过程发生I/O错误
     */
    @Override
    public int write(ByteBuffer src) throws IOException {
        return socketChannel.write(src);
    }
    /**
     * 从多个缓冲区向此通道写入数据
     *
     * @param srcs
     *         The buffers from which bytes are to be retrieved
     *
     * @return  写入的字节数
     * @throws IOException 在写入过程发生I/O错误
     */
    @Override
    public long write(ByteBuffer[] srcs) throws IOException {
        return write(srcs, 0, srcs.length);
    }

    /**
     * 从多个缓冲区往此通道写入数据
     *
     * @param srcs
     *         The buffers from which bytes are to be retrieved
     *
     * @param offset
     *         The offset within the buffer array of the first buffer from
     *         which bytes are to be retrieved; must be non-negative and no
     *         larger than {@code srcs.length}
     *
     * @param length
     *         The maximum number of buffers to be accessed; must be
     *         non-negative and no larger than
     *         {@code srcs.length}&nbsp;-&nbsp;{@code offset}
     *
     * @return 写入的字节数
     * @throws IOException 写入过程当中发生I/O错误
     */
    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        socketChannel.write(srcs, offset, length);
        return 0;
    }

    /**
     * 从此通道读取数据到给定的多个缓冲区当中
     *
     * @param dsts
     *         The buffers into which bytes are to be transferred
     *
     * @param offset
     *         The offset within the buffer array of the first buffer into
     *         which bytes are to be transferred; must be non-negative and no
     *         larger than {@code dsts.length}
     *
     * @param length
     *         The maximum number of buffers to be accessed; must be
     *         non-negative and no larger than
     *         {@code dsts.length}&nbsp;-&nbsp;{@code offset}
     *
     * @return 读取的字节数
     * @throws IOException 读取通道数据时发生I/O错误
     */
    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        return 0;
    }

    /**
     * 从此通道读取数据到给定的多个缓冲区当中
     *
     * @param dsts
     *         The buffers into which bytes are to be transferred
     *
     * @return 读取的字节数
     * @throws IOException 读取通道数据时发生I/O错误
     */
    @Override
    public long read(ByteBuffer[] dsts) throws IOException {
        return read(dsts, 0, dsts.length);
    }

    /**
     * 从此通道读取数据到指定的缓冲区
     *
     * @param dst
     *         The buffer into which bytes are to be transferred
     *
     * @return 读取的字节数
     * @throws IOException 读取通道数据时发生I/O错误
     */
    @Override
    public int read(ByteBuffer dst) throws IOException {
        return socketChannel.read(dst);
    }

    /**
     * 检查通道是否处于开启状态
     *
     * @return true此通道处于开启状态
     */
    @Override
    public boolean isOpen() {
        return socketChannel.isOpen();
    }

    /**
     * 关闭此通道
     *
     * @throws IOException 关闭通道时发生I/O错误
     */
    @Override
    public void close() throws IOException {
        socketChannel.close();
    }
}
