package com.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import com.utils.ListToStringTypeHandler;
import com.vo.UserVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;


@TableName(value = "user", autoResultMap = true)
@Data
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
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
	@TableField(typeHandler = ListToStringTypeHandler.class)
	private List<String> labels;

	/**
	 * 设备标签
	 */
	@TableField(typeHandler = ListToStringTypeHandler.class)
	private List<String> devices;

	/**
	 * 信誉值
	 */
	private Integer reputation;

	/**
	 * 能力值
	 */
	private Integer ability;

	public User(UserVO user){
		BeanUtils.copyProperties(user, this, "reputation", "ability");
	}

	public User(){
		this.setReputation(0);
		this.setAbility(0);
	}
}
