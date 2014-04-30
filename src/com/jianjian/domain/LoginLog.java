package com.jianjian.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="t_login_log")
public class LoginLog extends BaseDomain{
	private static final long serialVersionUID = -8182614439038571072L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="log_login_id")
	private int loginLogId;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(name="ip")
	private String ip;
	
	@Column(name="login_datetime")
	private Date loginDate;;

	
	public int getLoginLogId(){
		return loginLogId;
	}

	public void setLoginLogId(int loginLogId){
		this.loginLogId = loginLogId;
	}

	public User getUser(){
		return user;
	}

	public void setUser(User user){
		this.user = user;
	}

	public String getIp(){
		return ip;
	}

	public void setIp(String ip){
		this.ip = ip;
	}

	public Date getLoginDate(){
		return loginDate;
	}

	public void setLoginDate(Date loginDate){
		this.loginDate = loginDate;
	}
}
