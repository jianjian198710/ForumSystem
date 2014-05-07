package com.jianjian.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jianjian.service.UserService;

@Controller
public class RegisterController{

	@Autowired
	UserService userService;
	
	@RequestMapping("/register")
	public String register(){
		return null;
	}
}
