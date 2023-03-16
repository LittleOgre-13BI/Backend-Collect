package com.controller;

import javax.servlet.http.HttpServletRequest;

import com.vo.MyPage;
import com.vo.UserFormVO;
import com.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.service.UserService;
import com.utils.Response;


@RestController
@CrossOrigin(origins = "http://124.221.127.36", maxAge = 3600, allowCredentials="true",allowedHeaders = "*",methods = {RequestMethod.POST,RequestMethod.GET})
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
	
	/**
	 * 登录
	 */
	@PostMapping(value = "/login")
	public Response login(String username,String password,String userType) {
		return userService.login(username, password, userType);
	}
	
	/**
     * 注册
     */
	@PostMapping("/register")
    public Response register(String userType, @RequestBody UserVO user){
		return userService.register(user, userType);
    }
	
	/**
	 * 退出
	 */
	@RequestMapping("/logout")
	public Response logout(HttpServletRequest request) {
		return Response.ok("退出成功");
	}

    /**
     * 密码重置
     */
	@PostMapping(value = "/updatepassword")
    public Response updatePassword(Long uid, @RequestBody UserFormVO userFormVO){
		userFormVO.setUid(uid);
		return userService.updatePassword(userFormVO);
    }

	/**
	 * 修改用户基本信息
	 */

	@PostMapping(value = "/updateuser")
	public Response updateUser(Long uid, @RequestBody UserVO user){
		user.setUid(uid);
		return userService.updateUser(user);
	}

	/**
	 * 删除
	 */

	@PostMapping("/removeuser")
	public Response removeUser(Long uid){
		return userService.removeUser(uid);
	}


	/**
	 * 搜索所有用户
	 */
	@GetMapping("/searchallusers")
	public Response searchAllUsers(){ return Response.ok(); }

	/**
	 * 按昵称搜索用户
	 */
	@PostMapping("/searchusersbyname")
	public MyPage<UserVO> searchUsersByName(@RequestBody UserVO userVO, Long pageIndex, Long pageSize){
		return userService.searchUserByName(userVO, pageIndex, pageSize);
	}

	/**
	 * 按uid搜索用户
	 */
	@GetMapping("/searchuserbyuid")
	public Response searchUserByUid(Long uid){
		return userService.searchUserByUid(uid);
	}
}

