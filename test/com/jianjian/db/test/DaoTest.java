package com.jianjian.db.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate4.HibernateTemplate;

import com.jianjian.dao.UserDao;
import com.jianjian.domain.User;

public class DaoTest {

//	@Autowired
//	HibernateTemplate hibernateTemplate;
	
	//≤‚ ‘UserDao
	@Test
	public void test() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		UserDao userDao = (UserDao)ctx.getBean("userDao");
		System.out.println(userDao);
		Session session = userDao.getHibernateTemplate().getSessionFactory().openSession();
		Transaction tr = session.beginTransaction();
		User user = createUser();
		session.save(user);
		tr.commit();
		session.close();
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
