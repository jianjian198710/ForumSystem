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
import javax.servlet.http.HttpSession;

import com.jianjian.cons.CommonConstant;
import com.jianjian.domain.User;

/**
 * Servlet Filter implementation class ForumFilter
 */
@WebFilter("/ForumFilter")
public class ForumFilter implements Filter {
	private static final String FILTERED_REQUEST = "@@session_context_filtered_request";
	
	// ① 不需要登录即可访问的URI资源
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
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpSession session = httpRequest.getSession();
		//保证该过滤器在一次请求中只被调用一次
		if(session.getAttribute(FILTERED_REQUEST)!=null){
			System.out.println("该session的Filter已调用");
			System.out.println(session.getAttribute(FILTERED_REQUEST));
			chain.doFilter(request, response);
		}else{
			System.out.println("该session的Filter未调用");
			session.setAttribute(FILTERED_REQUEST, Boolean.TRUE);
			User userContext = getSessionUser(httpRequest);
			
			//用户未登录,且当前的URI需要登录才能访问
			//httpRequest.getQueryString() 比如发送http://localhost/test.do?a=b&c=d&e=f得到的是a=b&c=d&e=f
			if(userContext==null&&!isURILogin(httpRequest.getQueryString(),httpRequest)){
				System.out.println("bbb");
				String toUrl = httpRequest.getRequestURL().toString();
				if(httpRequest.getQueryString()!=null&&!httpRequest.getQueryString().isEmpty()){
					toUrl+="?"+httpRequest.getQueryString();
					System.out.println("aaa");
				}
				
				//将用户的请求URL保存在session中,用于登录成功后,跳到目标URL
				httpRequest.getSession().setAttribute(CommonConstant.LOGIN_TO_URL, toUrl);
				System.out.println("ccc");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
				return;
			}
			chain.doFilter(request, response);
		}
	}
	
	protected User getSessionUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute(CommonConstant.USER_CONTEXT);
	}
	
	//当前资源是否需要登录才能访问 true代表不需要登录即可访问
	private boolean isURILogin(String requestURI, HttpServletRequest request){
		//request.getContextPath()得到的是项目的名字
		if(request.getContextPath().equalsIgnoreCase(requestURI)||(request.getContextPath()+"/").equalsIgnoreCase(requestURI)){
			return true;
		}
		for(String uri:INHERENT_ESCAPE_URIS){
			if(requestURI!=null && requestURI.indexOf(uri)>=0){
				return true;
			}
		}
		return false;
	}
	

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
