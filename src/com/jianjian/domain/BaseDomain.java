package com.jianjian.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class BaseDomain implements Serializable{

	private static final long serialVersionUID = 1823688043987298208L;
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}

}
