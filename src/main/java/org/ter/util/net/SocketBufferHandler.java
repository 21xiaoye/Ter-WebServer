package org.ter.util.net;

import java.nio.ByteBuffer;

public class SocketBufferHandler {
    private volatile ByteBuffer readBuffer;
    private volatile ByteBuffer writeBuffer;
    public SocketBufferHandler(int readBufferSize, int writeBufferSize){
        readBuffer = ByteBuffer.allocate(readBufferSize);
        writeBuffer = ByteBuffer.allocate(writeBufferSize);
    }
    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }
    public boolean isReadBufferEmpty(){
        return readBuffer.remaining() == 0;
    }
    public boolean isWriteBufferEmpty(){
        return writeBuffer.remaining() == 0;
    }
    public ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }
    public void reset(){
        readBuffer.clear();
        writeBuffer.clear();
    }
}
