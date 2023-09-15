package com.lxqq.tools.common;

import cn.hutool.core.util.StrUtil;
import com.lxqq.tools.common.constant.HttpStatus;

/**
 * @author QinQiang
 * @date 2023/9/14
 * @copyright：Copyright ® 掌控网络. All right reserved.
 **/
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = -7924878864085184620L;
    private final int errorCode;

    public ServiceException(Throwable cause, String message) {
        super(generateMessageWithCodeAndMsg(HttpStatus.ERROR, message), cause);
        this.errorCode = HttpStatus.ERROR;
    }

    public ServiceException(Throwable cause) {
        super(generateUnknownMessageWithCode(), cause);
        this.errorCode = HttpStatus.ERROR;
    }

    public ServiceException(int errorCode, String message, Throwable cause) {
        super(generateMessageWithCodeAndMsg(errorCode, message), cause);
        this.errorCode = errorCode;
    }

    public ServiceException(String message, Throwable cause) {
        super(generateMessageWithCodeAndMsg(HttpStatus.ERROR, message), cause);
        this.errorCode = HttpStatus.ERROR;
    }

    public ServiceException(String message) {
        super(generateMessageWithCodeAndMsg(HttpStatus.ERROR, message));
        this.errorCode = HttpStatus.ERROR;
    }

    public ServiceException(int errorCode, String message) {
        super(generateMessageWithCodeAndMsg(errorCode, message));
        this.errorCode = errorCode;
    }

    public ServiceException(int errorCode) {
        super(generateMessageWithCode(errorCode));
        this.errorCode = errorCode;
    }

    public ServiceException(int errorCode, Throwable cause) {
        super(generateMessageWithCode(errorCode), cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    private static String generateMessageWithCodeAndMsg(int code, String message) {
        return StrUtil.isNotEmpty(message) ? message : "error code not init yet!errorCode=" + code;
    }

    private static String generateMessageWithCode(int code) {
        return generateMessageWithCodeAndMsg(code, "系统繁忙");
    }

    private static String generateUnknownMessageWithCode() {
        return generateMessageWithCode(HttpStatus.ERROR);
    }
}

