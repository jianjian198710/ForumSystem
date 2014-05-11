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
		//��֤�ù�������һ��������ֻ������һ��
		if(request!=null&&request.getAttribute(FILTERED_REQUEST)!=null){
			chain.doFilter(request, response);
		}else{
			request.setAttribute(FILTERED_REQUEST, Boolean.TRUE);
			HttpServletRequest httpRequest = (HttpServletRequest)request;
			User userContext = getSessionUser(httpRequest);
			
			//�û�δ��¼,�ҵ�ǰ��URI��Ҫ��¼���ܷ���
			//httpRequest.getQueryString() ���緢��http://localhost/test.do?a=b&c=d&e=f�õ�����a=b&c=d&e=f
			if(userContext==null&&!isURILogin(httpRequest.getQueryString(),httpRequest)){
				String toUrl = httpRequest.getRequestURL().toString();
				if(httpRequest.getQueryString()!=null&&!httpRequest.getQueryString().isEmpty()){
					toUrl+="?"+httpRequest.getQueryString();
				}else{
					System.out.println(httpRequest.getQueryString());
				}
				
				//���û�������URL������session��,���ڵ�¼�ɹ���,����Ŀ��URL
				httpRequest.getSession().setAttribute(CommonConstant.LOGIN_TO_URL, toUrl);
				
				request.getRequestDispatcher("/login.jsp").forward(request, response);
				return;
			}
			chain.doFilter(request, response);
		}
	}
	
	protected User getSessionUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute(CommonConstant.USER_CONTEXT);
	}
	
	//��ǰ��Դ�Ƿ���Ҫ��¼���ܷ��� true��������Ҫ��¼���ɷ���
	private boolean isURILogin(String requestURI, HttpServletRequest request){
		//request.getContextPath()�õ�������Ŀ������
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