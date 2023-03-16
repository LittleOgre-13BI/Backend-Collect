package com.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.utils.ListToStringTypeHandler;
import com.vo.MissionVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;


@TableName(value = "mission", autoResultMap = true)
@Data
public class Mission implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务编号
	 */
	@TableId
	private Long mid;

	private Long uid;

	/**
	 * 招募开始时间
	 */

	private Date recruitStart;

	/**
	 * 招募截至时间
	 */

	private Date recruitEnd;

	/**
	 * 最后一人接包的时间
	 */

	private Date lastFetchTime;

	/**
	 * 任务时限(单位为小时）
	 */

	private Integer timeLimit;

	/**
	 * 招募人数
	 */

	private Integer workerNum;

	/**
	 * 难度
	 */

	private Integer difficultyLevel;

	/**
	 * 任务描述
	 */

	private String description;

	/**
	 * 任务名
	 */
	private String name;

	/**
	 * 任务类型
	 */

	private String missionType;

	/**
	 * 标签
	 */
	@TableField(typeHandler = ListToStringTypeHandler.class)
	private List<String> labels;

	/**
	 * 设备描述
	 */

	private String deviceReq;

	/**
	 * 可执行文件路径
	 */

	private String exeUrl;

	/**
	 * 测试需求文档路径
	 */

	private String docUrl;

	/**
	 * 任务状态
	 */
	private String state;

	public Mission(MissionVO mission){
		BeanUtils.copyProperties(mission, this);
	}

	public Mission() {
	}

}
