package com.jianjian.db.test;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jianjian.domain.Board;
import com.jianjian.domain.User;
import com.jianjian.service.UserService;

public class DaoTest {
	//测试UserDao.getAllUsers()
	@Test
	public void testGetAllUsers() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		UserService userService = (UserService)ctx.getBean("userService");
		List<User> users = userService.getAllUsers();
		for(User user:users){
			System.out.println(user.getUserName());
		}
	}
	
	//测试userDao.queryAllUsers()级联关系
	@Test
	public void testQueryALlUsers(){
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		UserService userService = (UserService)ctx.getBean("userService");
		List<User> users = userService.queryAllUsers("ia");
		for(User user:users){
			System.out.println(user.getUserName());
		}
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
	
	public Board createBoard(){
		Board board = new Board();
		board.setBoardDesc("This is a fucking board");
		board.setBoardName("Fuck");
		board.setTopicNum(20);
		return board;
	}
}
