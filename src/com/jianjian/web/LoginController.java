package com.jianjian.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jianjian.cons.CommonConstant;
import com.jianjian.domain.User;
import com.jianjian.service.UserService;

/**
 * 
 * <br>
 * <b>类描述:</b>
 * 
 * <pre>
 *   论坛管理，这部分功能由论坛管理员操作，包括：创建论坛版块、指定论坛版块管理员、
 * 用户锁定/解锁。
 * </pre>
 * 
 * @see
 * @since
 */
@Controller
@RequestMapping(value="/login")
public class LoginController extends BaseController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/doLogin",method=RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request, String userName,String password){
		System.out.println("进入LoginController");
		User user = userService.getUserByUserName(userName);
		System.out.println(user);
		ModelAndView mav = new ModelAndView();
		//如果触发下面三个情况,还是跳到login.jsp
		if(request.getParameter("regist")!=null){
			System.out.println("没有用户名,开始注册");
			mav.setViewName("forward:/register.jsp");
		}else if(user==null){
			System.out.println("该用户不存在!");
			mav.addObject("errorMsg", "该用户不存在!");
			mav.setViewName("forward:/login.jsp");
		}else if(!user.getPassword().equals(password)){
			System.out.println("密码错误!");
			mav.addObject("errorMsg", "密码错误!");
			mav.setViewName("forward:/login.jsp");
		}else if(user.getLocked()==User.USER_LOCK){
			System.out.println("用户被锁定!");
			mav.addObject("errorMsg", "用户被锁定!");
			mav.setViewName("forward:/login.jsp");
		}else{
			user.setLastIp(request.getRemoteAddr());
			user.setLastVisit(new Date());
			userService.loginSuccess(user);
			setSessionUser(request,user);
			String toUrl = (String)request.getSession().getAttribute(CommonConstant.LOGIN_TO_URL);
			request.getSession().removeAttribute(CommonConstant.LOGIN_TO_URL);
			System.out.println("toUrl is"+toUrl);
			//如果当前会话中没有保存登录之前的请求URL，则直接跳转到主页
			if(StringUtils.isEmpty(toUrl)){
				System.out.println("toUrl is empty");
				toUrl = "/index.jsp";
			}
			mav.setViewName("forward:"+toUrl);
		}
		return mav;
	}
}
