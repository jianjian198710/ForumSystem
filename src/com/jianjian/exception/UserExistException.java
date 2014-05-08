package com.jianjian.exception;

public class UserExistException extends Exception{
	private static final long serialVersionUID = 2607585752348720821L;
	private static final String USER_NOT_EXIST = "该用户不存在!";

	public UserExistException(){
		super();
	}

	public UserExistException(String str){
		super(str);
	}
	
	public String Info(){
		return USER_NOT_EXIST;
	}
}
