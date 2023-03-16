package com.vo;

import com.po.Mission;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 招聘信息
 * 手机端接口返回实体辅助类 
 * （主要作用去除一些不必要的字段）
 * @author 
 * @email 
 * @date 2020-05-13 10:02:22
 */
@Data
public class MissionVO implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 任务编号
	 */
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

	public MissionVO(Mission mission){
		BeanUtils.copyProperties(mission, this);
	}

	public MissionVO() {
	}

}
