package com.fhs.redis.exception;

public class ConcurrentException extends RuntimeException {

	public ConcurrentException(String msg) {
		super(msg);
	}
	
}
