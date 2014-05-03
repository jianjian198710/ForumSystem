package com.jianjian.db.test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Test;

import com.jianjian.domain.Board;
import com.jianjian.domain.Post;
import com.jianjian.domain.Topic;
import com.jianjian.domain.User;

public class HibernateTest {
	//测试t_user
	@Test
	public void testUser() {
		User user = createUser();
		Board board = createBoard();
		
		Set<Board> boards = new HashSet<Board>();
		boards.add(board);
		user.setManBoards(boards);
		
		save(user);
	}
	
	//测试t_board
	@Test
	public void testBoard(){
		Board board = createBoard();
		User user = createUser();
		
		Set<User> users = new HashSet<User>();
		users.add(user);
		board.setUsers(users);
		
		save(board);
	}
	
	//测试t_topic
	@Test
	public void testTopic(){
		User user = createUser();
		Board board = createBoard();
		Topic topic = createTopic();
		
		topic.setBoard(board);
		topic.setUser(user);
		
		//Topic没有cascade到Board和User
		save(board);
		save(user);
		save(topic);
	}
	
	//测试t_post
	@Test
	public void testPost(){
		Post post = createPost();
		Topic topic = createTopic();
		User user = createUser();
		post.setTopic(topic);
		post.setUser(user);
		
		save(user);
		save(post);
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
	
	public Topic createTopic(){
		Topic topic = new Topic();
		topic.setCreateTime(new Date());
		topic.setLastPost(new Date());
		topic.setViews(10);
		topic.setTopicTitle("Fuck");
		topic.setReplies(20);
		topic.setDigest(Topic.NOT_DIGEST_TOPIC);
		return topic;
	}
	
	public Post createPost(){
		Post post = new Post();
		post.setCreateTime(new Date());
		post.setPostText("Fuck");
		post.setPostTitle("Fuck");
		return post;
	}
	
	public void save(Object object){
		Configuration cfg = new Configuration().configure();
		SessionFactory sf = cfg.buildSessionFactory(new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry());
		Session session = sf.openSession();
		Transaction tr = session.beginTransaction();
		//一定是persist 对应CascadeType.PERSIST这样才会级联 此处不能用save
		session.persist(object);
		tr.commit();
		session.close();
		sf.close();
	}
}
