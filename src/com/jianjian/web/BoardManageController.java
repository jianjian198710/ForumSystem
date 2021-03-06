package com.jianjian.web;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jianjian.cons.CommonConstant;
import com.jianjian.dao.Page;
import com.jianjian.domain.Board;
import com.jianjian.domain.Post;
import com.jianjian.domain.Topic;
import com.jianjian.domain.User;
import com.jianjian.service.ForumService;

//论坛的基本功能,包括发帖子,回帖,删帖,设精华
@Controller
public class BoardManageController extends BaseController{

	@Autowired
	private ForumService forumService;
	
	/**
	 * 列出论坛模块下的主题帖子
	 * 
	 * @param boardId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/board/listBoardTopics-{boardId}", method = RequestMethod.GET)
	public ModelAndView listBoardTopics(@PathVariable Integer boardId,@RequestParam(value = "pageNo", required = false) Integer pageNo) {
		System.out.println("进入BoardManageController.listBoardTopics()");
		ModelAndView view =new ModelAndView();
		Board board = forumService.getBoardById(boardId);
		pageNo = pageNo==null?1:pageNo;
		Page pagedTopic = forumService.getPagedTopics(boardId, pageNo,
				CommonConstant.PAGE_SIZE);
		view.addObject("board", board);
		view.addObject("pagedTopic", pagedTopic);
		view.setViewName("/listBoardTopics");
		return view;
	}
	
	/**
	 * 添加主题帖页面
	 * 
	 * @param boardId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/board/addTopicPage-{boardId}", method = RequestMethod.GET)
	public ModelAndView addTopicPage(@PathVariable Integer boardId) {
		System.out.println("进入BoardManageController.addTopicPage(),boardId="+boardId);
		ModelAndView view =new ModelAndView();
		view.addObject("boardId", boardId);
		view.setViewName("/addTopic");
		return view;
	}
	
	@RequestMapping(value = "/board/addTopic", method = RequestMethod.POST)
	public String addTopic(HttpServletRequest request,Topic topic, @RequestParam("boardId")Integer boardId){
		System.out.println("进入BoardManageController.addTopic()");
		User user = getSessionUser(request);
		Board board = forumService.getBoardById(boardId);
		System.out.println(user);
		System.out.println(board);
		
		Set<User> users = board.getUsers();
		Iterator<User> it = users.iterator();
		topic.setUser(user);
		while(it.hasNext()){
			User userInBoard = it.next();
			if(userInBoard.getUserId()==user.getUserId()){
				System.out.println("找到相同的User");
				topic.setUser(userInBoard);
			}
		}
		topic.setBoard(board);
		Date date = new Date();
		topic.setCreateTime(date);
		topic.setLastPost(date);
		System.out.println("Topic："+topic);
		forumService.addTopic(topic);
		String targetUrl = "/board/listBoardTopics-" + topic.getBoard().getBoardId()
				+ ".html";
		return "redirect:"+targetUrl;
	}
	
	/**
	 * 列出主题的所有帖子
	 * 
	 * @param topicId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/board/listTopicPosts-{topicId}", method = RequestMethod.GET)
	public ModelAndView listTopicPosts(@PathVariable Integer topicId,@RequestParam(value = "pageNo", required = false) Integer pageNo) {
		System.out.println("进入BoardManageController.listTopicPosts()");
		System.out.println("TopicId: "+topicId);
		ModelAndView view =new ModelAndView();
		Topic topic = forumService.getTopicByTopicId(topicId);
		System.out.println("Topic: "+topic);
		pageNo = pageNo==null?1:pageNo;
		Page pagedPost = forumService.getPagedPosts(topicId, pageNo,
				CommonConstant.PAGE_SIZE);
		// 为回复帖子表单准备数据
		view.addObject("topic", topic);
		view.addObject("pagedPost", pagedPost);
		view.setViewName("/listTopicPosts");
		return view;
	}

	/**
	 * 回复主题
	 * 
	 * @param request
	 * @param response
	 * @param post
	 * @return
	 */
	@RequestMapping(value = "/board/addPost")
	public String addPost(HttpServletRequest request, Post post, @RequestParam("topic.topicId") Integer topicId){
		System.out.println("进入BoardManageController.addPost()方法");
		System.out.println("TopicId: "+topicId);
		Topic topic  = forumService.getTopicByTopicId(topicId);
		post.setTopic(topic);
		post.setCreateTime(new Date());
		//防止userDao Session中同时存在两个相同User(分别是Topic中的User和HttpSession中的User,以至forumService.addPost中userDao.update(user)报Exception
		if(topic.getUser().getUserId()==getSessionUser(request).getUserId()){
			System.out.println("会引发userDao的Session中存在两个相同的user");
			post.setUser(topic.getUser());
		}else{
			post.setUser(getSessionUser(request));
		}
		System.out.println("Post: "+post);
		forumService.addPost(post);
		String targetUrl = "/board/listTopicPosts-"
				+ post.getTopic().getTopicId() + ".html";
		return "redirect:"+targetUrl;
	}

	/**
	 * 删除版块
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/board/removeBoard", method = RequestMethod.GET)
	public String removeBoard(@RequestParam("boardIds") String boardIds) {
		String[] arrIds = boardIds.split(",");
		for (int i = 0; i < arrIds.length; i++) {
			forumService.removeBoard(new Integer(arrIds[i]));
		}
		String targetUrl = "/index.html";
		return "redirect:"+targetUrl;
	}

	/**
	 * 删除主题
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/board/removeTopic", method = RequestMethod.GET)
	public String removeTopic(@RequestParam("topicIds") String topicIds,@RequestParam("boardId") String boardId) {
		String[] arrIds = topicIds.split(",");
		for (int i = 0; i < arrIds.length; i++) {
			forumService.removeTopic(new Integer(arrIds[i]));
		}
		String targetUrl = "/board/listBoardTopics-" + boardId + ".html";
		return "redirect:"+targetUrl;
	}

	/**
	 * 设置精华帖
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/board/makeDigestTopic", method = RequestMethod.GET)
	public String makeDigestTopic(@RequestParam("topicIds") String topicIds,@RequestParam("boardId") String boardId) {
		String[] arrIds = topicIds.split(",");
		for (int i = 0; i < arrIds.length; i++) {
			forumService.makeDigestTopic(new Integer(arrIds[i]));
		}
		String targetUrl = "/board/listBoardTopics-" + boardId + ".html";
		return "redirect:"+targetUrl;
	}
	
}
