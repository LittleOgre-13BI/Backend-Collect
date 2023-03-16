package com.serviceimpl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.utils.PageHelper;
import com.utils.Response;
import com.vo.MyPage;
import com.vo.UserFormVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.mapperservice.UserMapperService;
import com.po.User;
import com.service.UserService;
import com.vo.UserVO;

import java.util.Iterator;
import java.util.Map;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapperService, User> implements UserService {

	/**
	 * 用户登录
	 * @param userName 用户名
	 * @param password 密码
	 * @param userType 用户类型
	 * @return
	 */
	@Override
	public Response login(String userName, String password, String userType) {
		User user = getOne(new QueryWrapper<User>().eq("username", userName));
		if(user == null){
			return Response.error("用户名不正确");
		}
		if(!user.getUserType().equals(userType)){
			return Response.error("用户不存在");
		}
		if(!user.getPassword().equals(password)){
			return Response.error("密码不正确");
		}
		return Response.ok().put("uid", user.getUid());
	}

	/**
	 * 用户注册
	 * @param user 用户基本信息
	 * @param userType 用户类型：employee or employer
	 * @return
	 */
	@Override
	public Response register(UserVO user, String userType) {
		User checkUser = getOne(new QueryWrapper<User>().eq("username",user.getUsername()));
		if (checkUser != null)
			return Response.error("该用户名已被注册，请重新注册");

		user.setUserType(userType);
		User thisUser = new User(user);
		thisUser.setReputation(0);
		save(thisUser);
		return Response.ok();
	}

	/**
	 * 用户更改密码
	 * @param userFormVO 更改密码表单
	 * @return
	 */
	@Override
	public Response updatePassword(UserFormVO userFormVO) {
		if (!isUserExits(userFormVO.getUid())){
			return Response.error("用户不存在");
		}
		User user = getById(userFormVO.getUid());
		if(!user.getPassword().equals(userFormVO.getOldPassword())){
			return Response.error("密码不正确");
		}
		user.setPassword(userFormVO.getNewPassword());
		updateById(user);
		return Response.ok();
	}

	/**
	 * 用户更改基本信息
	 * @param user 用户基本信息
	 * @return
	 */
	@Override
	public Response updateUser(UserVO user) {
		if(!isUserExits(user.getUid())){
			return Response.error("用户不存在");
		}
		User oldUser, newUser;
		oldUser = getById(user.getUid());
		newUser = new User(user);
		newUser.setUsername(oldUser.getUsername());
		newUser.setPassword(oldUser.getPassword());
		newUser.setReputation(oldUser.getReputation());
		newUser.setAbility(oldUser.getAbility());
		newUser.setUserType(oldUser.getUserType());
//		newUser.setLabels(oldUser.getLabels());
//		newUser.setDevices(oldUser.getDevices());
		updateById(newUser);
		return Response.ok();
	}

	/**
	 * 删除用户
	 * @param uid 用户uid
	 * @return
	 */
	@Override
	public Response removeUser(Long uid) {
		if(!isUserExits(uid)){
			return Response.error("用户不存在");
		}
		removeById(uid);
		return Response.ok();
	}

	/**
	 * 根据用户username和nickname模糊查询用户列表
	 * case1:username与nickname都为空--查询所有用户
	 * case2:username与nickname其中一个为空--根据不为空的查询条件查询符合条件的用户
	 * case3:username与nickname都不为空--查询条件或连接
	 * @param userVO 查询条件：含username和nickname
	 * @param pageIndex 当前页
	 * @param pageSize 页大小
	 * @return
	 */
	@Override
	public MyPage<UserVO> searchUserByName(UserVO userVO, Long pageIndex, Long pageSize) {
		QueryWrapper<User> qw = new QueryWrapper<>();
		Map<String, Object> map = BeanUtil.beanToMap(userVO, true, true);
 		Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
		int i = 0;
		while(it.hasNext()){
			if(i++ > 0) qw.or();
			Map.Entry<String, Object> entry = it.next();
			if(! String.valueOf(entry.getValue()).isEmpty())
				qw.like(entry.getKey(), entry.getValue());
		}
		Page<User> page = new Page<>(pageIndex, pageSize);
		MyPage<User> users_page = new MyPage<>(page(page, qw));
		return PageHelper.convert(users_page, UserVO.class);
	}

	/**
	 * 根据用户uid查询用户
	 * @param uid 用户uid
	 * @return
	 */
	@Override
	public Response searchUserByUid(Long uid) {
		if (!isUserExits(uid)) return Response.error("用户不存在");
		return Response.ok().put("user", new UserVO(getById(uid)));
	}

	/**
	 * 递增活跃度
	 * @param uid 用户uid
	 */
	@Override
	public void increaseReputation(Long uid){
		User user = getById(uid);
		user.setReputation(user.getReputation()+1);
		updateById(user);
	}

	/**
	 * 根据uid判断用户是否存在
	 * @param uid 用户uid
	 * @return
	 */
	public boolean isUserExits(Long uid){
		return getById(uid) != null;
	}
}
