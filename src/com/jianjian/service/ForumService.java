package com.jianjian.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jianjian.dao.*;
import com.jianjian.domain.Board;
import com.jianjian.domain.MainPost;
import com.jianjian.domain.Post;
import com.jianjian.domain.Topic;
import com.jianjian.domain.User;
import com.jianjian.exception.NoUserOrBoardException;

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
		//相关板块Topic数量+1
		Board board = topic.getBoard();
		board.setTopicNum(board.getTopicNum()+1);
		boardDao.update(board);
		topicDao.save(topic);
		
		//发布Topic相当于发布主题帖
		MainPost post = topic.getMainPost();
		post.setTopic(topic);
		post.setCreateTime(new Date());
		post.setBoardId(topic.getBoard().getBoardId());
		post.setUser(topic.getUser());
		post.setPostTitle(topic.getTopicTitle());
		postDao.save(post);
		
		//用户分数+10
		User user = topic.getUser();
		user.setCredit(user.getCredit()+10);
		userDao.update(user);
		System.out.println("添加Topic成功!");
	}
	
	public Board getBoardById(int boardId){
		return boardDao.get(boardId);
	}
	
	public void updateTopic(Topic topic){
		topicDao.update(topic);
	}
	
	public void removeTopic(int topicId){
		Topic topic = topicDao.get(topicId);
		Board board = topic.getBoard();
		User user = topic.getUser();
		//版块Board话题数-1 用户分数-10
		board.setTopicNum(board.getTopicNum()-1);
		user.setCredit(user.getCredit()-10);

		boardDao.update(board);
		userDao.update(user);
		topicDao.remove(topic);
		
		System.out.println("删除Topic成功");
		//删除Topic的相关帖子
		postDao.deleteTopicPosts(topicId);
	}
	
	public Topic getTopicByTopicId(int topicId){
		return topicDao.get(topicId);
	}
	
	public void addPost(Post post){
		postDao.save(post);
		Topic topic = post.getTopic();
		User user = post.getUser();
		
		//用户分数+5 主题回复数+1 更新话题最后回复时间
		user.setCredit(user.getCredit()+5);
		topic.setReplies(topic.getReplies()+1);
		topic.setLastPost(new Date());
		
		userDao.update(user);
		topicDao.update(topic);
	}
	
	public void updatePost(Post post){
		postDao.update(post);
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
		//主题加精
		Topic topic  = topicDao.get(topicId);
		topic.setDigest(Topic.DIGEST_TOPIC);
		
		//作者+50分
		User user = topic.getUser();
		user.setCredit(user.getCredit()+50);
		
		userDao.update(user);
		topicDao.update(topic);
	}
	
	//加板块管理员
	public void addBoardManager (int boardId,String userName)throws NoUserOrBoardException{
		User user = userDao.getUserByUserName(userName);
		Board board = boardDao.get(boardId);
		if(user==null||board==null){
			throw new NoUserOrBoardException("没有指定User或者Board");
		}else{
			user.getManBoards().add(board);
			board.getUsers().add(user);
			userDao.update(user);
			//cascadeType是persist merge不需要boardDao.update(board)
		}
	}
	
	public List<Board> listAllBoards(){
		return boardDao.getAllBoards();
	}
	
	/**
	 * 获取论坛版块某一页主题帖，以最后回复时间降序排列
	 * @param boardId
	 * @return
	 */
    public Page getPagedTopics(int boardId,int pageNo,int pageSize){
		return topicDao.getPagedTopics(boardId,pageNo,pageSize);
    }
    
    /**
     * 获取同主题每一页帖子，以最后回复时间降序排列
     * @param boardId
     * @return
     */
    public Page getPagedPosts(int topicId,int pageNo,int pageSize){
        return postDao.getPagedPosts(topicId,pageNo,pageSize);
    }    
    

	/**
	 * 查找出所有包括标题包含title的主题帖
	 * 
	 * @param title
	 *            标题查询条件
	 * @return 标题包含title的主题帖
	 */
	public Page queryTopicByTitle(String title,int pageNo,int pageSize) {
		return topicDao.queryTopicByTitle(title,pageNo,pageSize);
	}
}