package com.jianjian.db.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.jianjian.domain.User;

public class DaoTest {

	@Autowired
	HibernateTemplate hibernateTemplate;
	
	//≤‚ ‘UserDao
	@Test
	public void test() {
		User user = createUser();
		hibernateTemplate.save(user);
	}

	public User createUser(){
		User user = new User();
		user.setUserName("jianjian");
		user.setPassword("198710");
		user.setUserType(User.NORMAL_USER);
		user.setLocked(User.USER_UNLOCK);
		user.setCredit(100);
		user.setLastVisit(new Date());
		user.setLastIp("192.168.0.1");
		return user;
	}
}
