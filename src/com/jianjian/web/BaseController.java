package com.jianjian.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;

import com.jianjian.cons.CommonConstant;
import com.jianjian.domain.User;

public class BaseController{
	protected static final String ERROR_MSG_KEY = "errorMsg";

	/**
	 * ��ȡ������Session�е��û�����
	 * 
	 * @param request
	 * @return
	 */
	protected User getSessionUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute(
				CommonConstant.USER_CONTEXT);
	}
   
	/**
	 * �����û�����Session��
	 * @param request
	 * @param user
	 */
	protected void setSessionUser(HttpServletRequest request,User user) {
		request.getSession().setAttribute(CommonConstant.USER_CONTEXT,
				user);
	}
	
	/**
	 * ��ȡ����Ӧ�ó����url����·��
	 * 
	 * @param request
	 * @param url
	 *            ��"/"��ͷ��URL��ַ
	 * @return ����Ӧ�ó����url����·��
	 */
	public final String getAppbaseUrl(HttpServletRequest request, String url) {
		Assert.hasLength(url, "url����Ϊ��");
		Assert.isTrue(url.startsWith("/"), "������/��ͷ");
		return request.getContextPath() + url;
	}
	
	
}
