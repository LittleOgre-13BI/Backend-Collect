package com.vo;

import com.po.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;


import java.io.Serializable;
import java.util.List;


@Data
public class UserVO implements Serializable {
	private static final long serialVersionUID = 1L;


	private Long uid;

	/**
	 * 昵称
	 */

	private String nickname;

	/**
	 * 用户名
	 */
	
	private String username;
		
	/**
	 * 密码
	 */
	
	private String password;
		
	/**
	 * 用户角色
	 */
	
	private String userType;
		
	/**
	 * 电话
	 */
	private String phoneNumber;
		
	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 公司名称
	 */
	private String companyName;

	/**
	 * 测试偏好标签
	 */
	private List<String> labels;

	/**
	 * 用户测试设备标签
	 */
	private List<String> devices;

	public UserVO(User user){
		BeanUtils.copyProperties(user, this);
	}

	public UserVO(){}

}
