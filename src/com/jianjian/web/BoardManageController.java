package com.jianjian.web;

import java.util.Date;

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

//��̳�Ļ�������,����������,����,ɾ��,�辫��
@Controller
public class BoardManageController extends BaseController{

	@Autowired
	private ForumService forumService;
	
	/**
	 * �г���̳ģ���µ���������
	 * 
	 * @param boardId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/board/listBoardTopics-{boardId}", method = RequestMethod.GET)
	public ModelAndView listBoardTopics(@PathVariable Integer boardId,@RequestParam(value = "pageNo", required = false) Integer pageNo) {
		System.out.println("����BoardManageController.listBoardTopics()");
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
	 * ���������ҳ��
	 * 
	 * @param boardId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/board/addTopicPage-{boardId}", method = RequestMethod.GET)
	public ModelAndView addTopicPage(@PathVariable Integer boardId) {
		System.out.println("����BoardManageController.addTopicPage(),boardId="+boardId);
		ModelAndView view =new ModelAndView();
		view.addObject("boardId", boardId);
		view.setViewName("/addTopic");
		return view;
	}
	
	@RequestMapping(value = "/board/addTopic", method = RequestMethod.POST)
	public String addTopic(HttpServletRequest request,Topic topic, @RequestParam("boardId")Integer boardId){
		System.out.println("����BoardManageController.addTopic()");
		User user = getSessionUser(request);
		Board board = forumService.getBoardById(boardId);
		System.out.println(user);
		System.out.println(board);
		topic.setUser(user);
		topic.setBoard(board);
		Date date = new Date();
		topic.setCreateTime(date);
		topic.setLastPost(date);
		forumService.addTopic(topic);
		String targetUrl = "/board/listBoardTopics-" + topic.getBoard().getBoardId()
				+ ".html";
		return "redirect:"+targetUrl;
	}
	
	/**
	 * �г��������������
	 * 
	 * @param topicId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/board/listTopicPosts-{topicId}", method = RequestMethod.GET)
	public ModelAndView listTopicPosts(@PathVariable Integer topicId,@RequestParam(value = "pageNo", required = false) Integer pageNo) {
		System.out.println("����BoardManageController.listTopicPosts()");
		System.out.println("TopicId: "+topicId);
		ModelAndView view =new ModelAndView();
		Topic topic = forumService.getTopicByTopicId(topicId);
		System.out.println("Topic: "+topic);
		pageNo = pageNo==null?1:pageNo;
		Page pagedPost = forumService.getPagedPosts(topicId, pageNo,
				CommonConstant.PAGE_SIZE);
		// Ϊ�ظ����ӱ�׼������
		view.addObject("topic", topic);
		view.addObject("pagedPost", pagedPost);
		view.setViewName("/listTopicPosts");
		return view;
	}

	/**
	 * �ظ�����
	 * 
	 * @param request
	 * @param response
	 * @param post
	 * @return
	 */
	@RequestMapping(value = "/board/addPost")
	public String addPost(HttpServletRequest request, Post post) {
		post.setCreateTime(new Date());
		post.setUser(getSessionUser(request));
		forumService.addPost(post);
		String targetUrl = "/board/listTopicPosts-"
				+ post.getTopic().getTopicId() + ".html";
		return "redirect:"+targetUrl;
	}

	/**
	 * ɾ�����
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
	 * ɾ������
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
	 * ���þ�����
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
