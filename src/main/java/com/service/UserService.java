package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.po.User;
import com.utils.Response;
import com.vo.MyPage;
import com.vo.UserFormVO;
import com.vo.UserVO;


public interface UserService extends IService<User>{

	Response login(String userName, String password, String userType);

	Response register(UserVO user, String userType);

	Response updatePassword(UserFormVO userFormVO);

	Response updateUser(UserVO user);

	Response removeUser(Long uid);

	MyPage<UserVO> searchUserByName(UserVO userVO, Long pageIndex, Long pageSize);

	Response searchUserByUid(Long uid);

	boolean isUserExits(Long uid);

	void increaseReputation(Long uid);
}

