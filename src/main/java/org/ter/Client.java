package org.ter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) {
        try {
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress( 6771));
            socketChannel.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(256);
            buffer.put("Hello, Server!".getBytes());
            buffer.flip();
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
