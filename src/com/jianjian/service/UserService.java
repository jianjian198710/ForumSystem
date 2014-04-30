package com.jianjian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jianjian.dao.LoginLogDao;
import com.jianjian.dao.UserDao;
import com.jianjian.domain.LoginLog;
import com.jianjian.domain.User;

/**
 * �û�����������������ѯ�û���ע���û��������û��Ȳ���
 *
 */
@Service
public class UserService {
	private static final String USER_NOT_EXIST = "���û�������!";
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private LoginLogDao loginLogDao;
	
	public void register(User user){
		User u = userDao.getUserByUserName(user.getUserName());
		//����û��Ѵ���
		if(u!=null){
			System.out.println(USER_NOT_EXIST);
		}
		user.setCredit(100);
		user.setUserType(User.NORMAL_USER);
		user.setLocked(User.USER_UNLOCK);
		userDao.save(user);
		System.out.println("�û�ע��ɹ�!");
	}
	
	public void lockUser(String userName){
		User user  = userDao.getUserByUserName(userName);
		if(user!=null){
			user.setLocked(User.USER_LOCK);
			userDao.update(user);
			System.out.println("�û��Ѿ�������!");
		}else{
			System.out.println(USER_NOT_EXIST);
		}
	}
	
	public void unLockUser(String userName){
		User user = userDao.getUserByUserName(userName);
		if(user!=null&&user.getLocked()==User.USER_LOCK){
			user.setLocked(User.USER_UNLOCK);
			userDao.update(user);
			System.out.println("�û��Ѿ�������!");
		}else{
			System.out.println(USER_NOT_EXIST+"����û�б�����");
		}
	}
	
	public void loginSuccess(User user){
		//��¼�ɹ��û�����+5
		user.setCredit(user.getCredit()+5);
		//�����û���¼��Ϣ
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