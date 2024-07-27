package org.ter.exception;

/**
 * 接收到无效数据的异常处理类
 */
public class InvalidDataException extends Exception {
    private static final long serialVersionUID = 3731842424390998726L;


    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(Throwable t) {
        super(t);
    }

    public InvalidDataException(int closeCode, String s, Throwable t) {
        super(s, t);
    }
}

