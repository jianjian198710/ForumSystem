package com.jianjian.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jianjian.domain.Topic;
import com.jianjian.domain.User;
import com.jianjian.service.ForumService;

//论坛的基本功能,包括发帖子,回帖,删帖,设精华
@Controller
public class BoardManageController extends BaseController{

	@Autowired
	private ForumService forumService;
	
	public ModelAndView listBoardTopics(){
		return null;
	}
	
	@RequestMapping(value = "/board/addTopic", method = RequestMethod.POST)
	public String addTopicPage(HttpServletRequest request,Topic topic){
		User user = getSessionUser(request);
		topic.setUser(user);
		Date date = new Date();
		topic.setCreateTime(date);
		topic.setLastPost(date);
		forumService.addTopic(topic);
		String targetUrl = "/board/listBoardTopics-" + topic.getBoard().getBoardId()
				+ ".html";
		return "redirect:"+targetUrl;
	}
	
	public ModelAndView addTopic(){
		return null;
	}
	
	
}
