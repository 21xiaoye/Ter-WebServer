package org.ter.exception;

/**
 * 接收到无效数据的异常处理类
 */
public class InvalidDataException extends Exception{
    private static final long serialVersionUID = 3731842424390998726L;
    private final int closeCode;

    public InvalidDataException(int closeCode) {
        this.closeCode = closeCode;
    }

    public InvalidDataException(int closeCode, String message){
        super(message);
        this.closeCode = closeCode;
    }

    public InvalidDataException(int closeCode, Throwable t) {
        super(t);
        this.closeCode = closeCode;
    }

    public InvalidDataException(int closeCode, String s, Throwable t) {
        super(s, t);
        this.closeCode = closeCode;
    }

    public int getCloseCode() {
        return closeCode;
    }
}

