package com.jianjian.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jianjian.domain.Board;
import com.jianjian.domain.User;
import com.jianjian.exception.NoUserOrBoardException;
import com.jianjian.service.ForumService;
import com.jianjian.service.UserService;

//��̳����,�����̳���,ָ����̳������Ա,���û���������/����
public class ForumManageController extends BaseController{

	@Autowired
	private ForumService forumService;
	@Autowired
	private UserService userService;
	
	/**
	 * �г����е���̳ģ��
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView listAllBoards(){
		ModelAndView view = new ModelAndView();
		List<Board> boards = forumService.listAllBoards();
		view.addObject(boards);
		view.setViewName("/listAllBoards");
		return view;
	}
	
	/**
	 *  ���һ��������
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/forum/addBoardPage", method = RequestMethod.GET)
	public String addBoardPage() {
		return "/addBoard";
	}
	
	/**
	 *  ���һ��������
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/forum/addBoard", method = RequestMethod.POST)
	public String addBoard(Board board) {
		forumService.addBoard(board);
		return "/addBoardSuccess";
	}
	
	/**
	 * ָ����̳����Ա��ҳ��
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/forum/setBoardManagerPage", method = RequestMethod.GET)
	public ModelAndView setBoardManagerPage() {
		ModelAndView view =new ModelAndView();
		List<Board> boards = forumService.listAllBoards();
		List<User> users = userService.getAllUsers();
		view.addObject("boards", boards);
		view.addObject("users", users);
		view.setViewName("/setBoardManager");
		return view;
	}
    /**
     * ���ð�����
     * @param request
     * @param response
     * @return
     */
	@RequestMapping(value = "/forum/setBoardManager", method = RequestMethod.POST)
	public ModelAndView setBoardManager(@RequestParam("userName") String userName
			,@RequestParam("boardId") String boardId) {
		ModelAndView view =new ModelAndView();
		try{
			forumService.addBoardManager(Integer.parseInt(boardId), userName);
			view.setViewName("/success");
		}catch (NoUserOrBoardException e){
			view.addObject("errorMsg", "�û���(" + userName
					+ ")������");
			view.setViewName("/fail");
		}
		return view;
	}
	
	/**
	 * �û���������������ҳ��
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/forum/userLockManagePage", method = RequestMethod.GET)
	public ModelAndView userLockManagePage() {
		ModelAndView view = new ModelAndView();
		List<User> users = userService.getAllUsers();
		view.addObject("users",users);
		view.setViewName("/forum/userLockManage");
		return view;
	}
	/**
	 * �û������������趨
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/forum/userLockManagePage", method = RequestMethod.GET)
	public ModelAndView userLockManage(@RequestParam("userName") String userName
			,@RequestParam("locked") String locked) {
		ModelAndView view = new ModelAndView();
		User user = userService.getUserByUserName(userName);
		if(user==null){
			view.addObject("errorMsg","�û���("+userName+")������");
			view.setViewName("/fail");
		}else{
			user.setLocked(Integer.parseInt(locked));
			userService.update(user);
			view.setViewName("/success");
		}
		return view;
	}
}
