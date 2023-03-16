package com.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.po.FetchMission;
import com.utils.ListToStringTypeHandler;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 应聘信息
 * 手机端接口返回实体辅助类 
 * （主要作用去除一些不必要的字段）
 * @author 
 * @email 
 * @date 2020-05-13 10:02:23
 */
@Data
public class FetchMissionVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long fid;

	private Long uid;

	private Long mid;
	 			
	/**
	 * 接包时间
	 */
	
	private Date fetchDate;
		
	/**
	 * 任务时限（单位小时）
	 */
	
	private Integer timeLimit;

	/**
	 * 提交时间
	 */

	private Date submitTime;

	/**
	 * 报告主题
	 */
	private String title;
		
	/**
	 * 截图路径
	 */
	private String picture1;

	private List<String> coordinate1;

	private String picture2;

	private List<String> coordinate2;

	private String picture3;

	private List<String> coordinate3;

	private String picture4;

	private List<String> coordinate4;

	/**
	 * 故障描述
	 */
	
	private String bugDescription;
		
	/**
	 * 故障复现步骤
	 */
	
	private String bugRecurrentSteps;
		
	/**
	 * 设备信息
	 */
	
	private String deviceInformation;

	/**
	 * 点赞数
	 */

	private Integer likes;

	/**
	 * 点踩数
	 */

	private Integer dislikes;

	/**
	 * 平均分
	 */
	private double score;

	/**
	 * 是否为低质量报告
	 */
	private String isBad;


	public FetchMissionVO(FetchMission fetchMission){
		BeanUtils.copyProperties(fetchMission, this);
	}

	public FetchMissionVO(){
		setDislikes(0);
		setLikes(0);
		setScore(0);
		setIsBad("False");
	}
}
