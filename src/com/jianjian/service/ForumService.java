package com.jianjian.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jianjian.dao.*;
import com.jianjian.domain.Board;
import com.jianjian.domain.MainPost;
import com.jianjian.domain.Post;
import com.jianjian.domain.Topic;
import com.jianjian.domain.User;

@Service
public class ForumService {
	@Autowired
	private TopicDao topicDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private BoardDao boardDao;
	@Autowired
	private PostDao postDao;
	
	public void addBoard(Board board){
		boardDao.save(board);
	}
	
	public void removeBoard(int boardId){
		Board board = boardDao.get(boardId);
		boardDao.remove(board);
	}
	
	public void addTopic(Topic topic){
		Board board = topic.getBoard();
		board.setTopicNum(board.getTopicNum()+1);
		boardDao.update(board);
		topicDao.save(topic);
		
		//����Topic�൱�ڷ���������
		MainPost post = topic.getMainPost();
		post.setTopic(topic);
		post.setCreateTime(new Date());
		post.setBoardId(topic.getBoard().getBoardId());
		post.setUser(topic.getUser());
		post.setPostTitle(topic.getTopicTitle());
		postDao.save(post);
		
		//�û�����+10
		User user = topic.getUser();
		user.setCredit(user.getCredit()+10);
		userDao.update(user);
		System.out.println("����Topic�ɹ�!");
	}
	
	public void removeTopic(int topicId){
		Topic topic = topicDao.get(topicId);
		Board board = topic.getBoard();
		User user = topic.getUser();
		//��黰����-1 �û�����-10
		board.setTopicNum(board.getTopicNum()-1);
		user.setCredit(user.getCredit()-10);

		boardDao.update(board);
		userDao.update(user);
		topicDao.remove(topic);
		
		System.out.println("ɾ��Topic�ɹ�");
		//POST��Topic CascaseAll ���Ի��Զ�ɾ��
//		postDao.deleteTopicPosts(topicId);
	}
	
	public void addPost(Post post){
		postDao.save(post);
		Topic topic = post.getTopic();
		User user = post.getUser();
		
		//�û�����+5 ����ظ���+1 ���»������ظ�ʱ��
		user.setCredit(user.getCredit()+5);
		topic.setReplies(topic.getReplies()+1);
		topic.setLastPost(new Date());
		
		userDao.update(user);
		topicDao.update(topic);
	}
	
	public void removePost(int postId){
		Post post = postDao.get(postId);
		Topic topic = post.getTopic();
		User user = post.getUser();
		
		user.setCredit(user.getCredit()-5);
		topic.setReplies(topic.getReplies()-1);
		topic.setLastPost(new Date());
		
		postDao.remove(post);
		userDao.update(user);
		topicDao.update(topic);
	}
	
	public void makeDigestTopic(int topicId){
		//����Ӿ�
		Topic topic  = topicDao.get(topicId);
		topic.setDigest(Topic.DIGEST_TOPIC);
		
		//����+50��
		User user = topic.getUser();
		user.setCredit(user.getCredit()+50);
		
		userDao.update(user);
		topicDao.update(topic);
	}
}