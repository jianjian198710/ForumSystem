package com.jianjian.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jianjian.domain.User;
import com.jianjian.exception.UserExistException;
import com.jianjian.service.UserService;

@Controller
public class RegisterController extends BaseController{

	@Autowired
	UserService userService;
	
	@RequestMapping(value="/register",method = RequestMethod.POST)
	public ModelAndView register(HttpServletRequest request, User user){
		System.out.println("进入RegisterController,userName:"+user.getUserName());
		ModelAndView view = new ModelAndView();
		view.setViewName("/success");
		try{
			userService.register(user);
		}catch(UserExistException e){
			e.Info();
			view.addObject("errorMsg", "用户名已经存在!");
			view.setViewName("forward:/register.jsp");
		}
		setSessionUser(request,user);
		return view;
	}
}
