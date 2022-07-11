package com.upking.project.common.exception;

/**
 * @author king
 * @version 1.0
 * @className CommonException
 * @description TODO
 * @date 2022/7/7
 */
public class CommonException extends BaseException {

    private static final long serialVersionUID = -6003749635161707230L;

    public CommonException(int errorCode) {
        super(errorCode + "");
    }

    public CommonException(int errorCode, Throwable cause) {
        super(errorCode + "", cause);
    }

    public CommonException(int errorCode, String messages) {
        super(errorCode + "", messages);
    }

    public CommonException(int errorCode, Throwable cause, String messages) {
        super(errorCode + "", cause, messages);
    }

    public CommonException(String errorCode) {
        super(errorCode);
    }

    public CommonException(String errorCode, String message) {
        super(errorCode, message);
    }

    public CommonException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public CommonException(String errorCode, Throwable cause, String message) {
        super(errorCode, cause, message);
    }
}
