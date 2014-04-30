package com.jianjian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jianjian.dao.LoginLogDao;
import com.jianjian.dao.UserDao;
import com.jianjian.domain.LoginLog;
import com.jianjian.domain.User;

/**
 * 用户管理服务器，负责查询用户、注册用户、锁定用户等操作
 *
 */
@Service
public class UserService {
	private static final String USER_NOT_EXIST = "该用户不存在!";
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private LoginLogDao loginLogDao;
	
	public void register(User user){
		User u = userDao.getUserByUserName(user.getUserName());
		//如果用户已存在
		if(u!=null){
			System.out.println(USER_NOT_EXIST);
		}
		user.setCredit(100);
		user.setUserType(User.NORMAL_USER);
		user.setLocked(User.USER_UNLOCK);
		userDao.save(user);
		System.out.println("用户注册成功!");
	}
	
	public void lockUser(String userName){
		User user  = userDao.getUserByUserName(userName);
		if(user!=null){
			user.setLocked(User.USER_LOCK);
			userDao.update(user);
			System.out.println("用户已经被锁定!");
		}else{
			System.out.println(USER_NOT_EXIST);
		}
	}
	
	public void unLockUser(String userName){
		User user = userDao.getUserByUserName(userName);
		if(user!=null&&user.getLocked()==User.USER_LOCK){
			user.setLocked(User.USER_UNLOCK);
			userDao.update(user);
			System.out.println("用户已经被解锁!");
		}else{
			System.out.println(USER_NOT_EXIST+"或者没有被锁定");
		}
	}
	
	public void loginSuccess(User user){
		//登录成功用户分数+5
		user.setCredit(user.getCredit()+5);
		//更新用户登录信息
		LoginLog loginLog = new LoginLog();
		loginLog.setUser(user);
		loginLog.setIp(user.getLastIp());
		loginLog.setLoginDate(user.getLastVisit());
		userDao.update(user);
		loginLogDao.save(loginLog);
	}
	
	public User getUserByUserName(String userName){
		return userDao.getUserByUserName(userName);
	}
}