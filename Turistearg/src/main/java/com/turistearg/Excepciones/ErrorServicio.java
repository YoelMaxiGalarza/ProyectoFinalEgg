package com.turistearg.Excepciones;

public class ErrorServicio extends Exception {

	public ErrorServicio(String msg) {
		super(msg);
	}
	
	public ErrorServicio(String msg, Throwable cause) {
		super(msg, cause);
	}

}
