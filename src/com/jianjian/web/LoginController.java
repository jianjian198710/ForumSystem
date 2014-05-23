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
 * <b>������:</b>
 * 
 * <pre>
 *   ��̳�����ⲿ�ֹ�������̳����Ա������������������̳��顢ָ����̳������Ա��
 * �û�����/������
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
		System.out.println("����LoginController");
		User user = userService.getUserByUserName(userName);
		System.out.println(user);
		ModelAndView mav = new ModelAndView();
		//������������������,��������login.jsp
		if(request.getParameter("regist")!=null){
			System.out.println("û���û���,��ʼע��");
			mav.setViewName("forward:/register.jsp");
		}else if(user==null){
			System.out.println("���û�������!");
			mav.addObject("errorMsg", "���û�������!");
			mav.setViewName("forward:/login.jsp");
		}else if(!user.getPassword().equals(password)){
			System.out.println("�������!");
			mav.addObject("errorMsg", "�������!");
			mav.setViewName("forward:/login.jsp");
		}else if(user.getLocked()==User.USER_LOCK){
			System.out.println("�û�������!");
			mav.addObject("errorMsg", "�û�������!");
			mav.setViewName("forward:/login.jsp");
		}else{
			user.setLastIp(request.getRemoteAddr());
			user.setLastVisit(new Date());
			userService.loginSuccess(user);
			setSessionUser(request,user);
			String toUrl = (String)request.getSession().getAttribute(CommonConstant.LOGIN_TO_URL);
			request.getSession().removeAttribute(CommonConstant.LOGIN_TO_URL);
			System.out.println("toUrl is"+toUrl);
			//�����ǰ�Ự��û�б����¼֮ǰ������URL����ֱ����ת����ҳ
			if(StringUtils.isEmpty(toUrl)){
				System.out.println("toUrl is empty");
				toUrl = "/index.jsp";
			}
			mav.setViewName("forward:"+toUrl);
		}
		return mav;
	}
}
