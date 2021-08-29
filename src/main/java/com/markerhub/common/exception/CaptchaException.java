package com.markerhub.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * security的运行时异常类 用于抛出带信息的异常
 */
public class CaptchaException extends AuthenticationException {

	public CaptchaException(String msg) {
		super(msg);
	}
}
