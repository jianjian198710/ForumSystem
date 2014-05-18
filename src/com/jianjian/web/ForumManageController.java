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

//论坛管理,添加论坛版块,指定论坛版块管理员,对用户进行锁定/解锁
public class ForumManageController extends BaseController{

	@Autowired
	private ForumService forumService;
	@Autowired
	private UserService userService;
	
	/**
	 * 列出所有的论坛模块
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
	 *  添加一个主题帖
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/forum/addBoardPage", method = RequestMethod.GET)
	public String addBoardPage() {
		return "/addBoard";
	}
	
	/**
	 *  添加一个主题帖
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
	 * 指定论坛管理员的页面
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
     * 设置版块管理
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
			view.addObject("errorMsg", "用户名(" + userName
					+ ")不存在");
			view.setViewName("/fail");
		}
		return view;
	}
	
	/**
	 * 用户锁定及解锁管理页面
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
	 * 用户锁定及解锁设定
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
			view.addObject("errorMsg","用户名("+userName+")不存在");
			view.setViewName("/fail");
		}else{
			user.setLocked(Integer.parseInt(locked));
			userService.update(user);
			view.setViewName("/success");
		}
		return view;
	}
}
