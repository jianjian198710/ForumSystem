package com.jianjian.exception;

public class NoUserOrBoardException extends Exception{
	private static final long serialVersionUID = -824953885887717604L;

	public NoUserOrBoardException(){
		super();
	}
	
	public NoUserOrBoardException(String message){
		super(message);
	}
}
