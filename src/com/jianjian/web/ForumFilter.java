package com.jianjian.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.jianjian.cons.CommonConstant;
import com.jianjian.domain.User;

/**
 * Servlet Filter implementation class ForumFilter
 */
@WebFilter("/ForumFilter")
public class ForumFilter implements Filter {
	private static final String FILTERED_REQUEST = "@@session_context_filtered_request";
	
	// �� ����Ҫ��¼���ɷ��ʵ�URI��Դ
	private static final String[] INHERENT_ESCAPE_URIS = { "/index.jsp",
			"/index.html", "/login.jsp", "/login/doLogin.html",
			"/register.jsp", "/register.html", "/board/listBoardTopics-",
			"/board/listTopicPosts-" };
	
    /**
     * Default constructor. 
     */
    public ForumFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// ��-1 ��֤�ù�������һ��������ֻ������һ��
		if(request!=null&&request.getAttribute(FILTERED_REQUEST)!=null){
			chain.doFilter(request, response);
		}else{
			request.setAttribute(FILTERED_REQUEST, Boolean.TRUE);
			HttpServletRequest httpRequest = (HttpServletRequest)request;
			User userContext = getSessionUser(httpRequest);
//			if(userContext==null&&!)
			
		}
		
	}
	
	protected User getSessionUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute(CommonConstant.USER_CONTEXT);
	}
	
	//��ǰ��Դ�Ƿ���Ҫ��¼���ܷ���
	private boolean isURILogin(String requestURI, HttpServletRequest request){
		if(request.getContextPath().equalsIgnoreCase(requestURI)||(request.getContextPath()+"/").equalsIgnoreCase(requestURI)){
			;
		}
	}
	

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
