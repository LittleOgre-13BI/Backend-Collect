package com.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.utils.ListToStringTypeHandler;
import com.vo.FetchMissionVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;


/**
 * 应聘信息
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2020-05-13 10:02:23
 */
@Data
@TableName("fetchmission")
public class FetchMission implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
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

	@TableField(typeHandler = ListToStringTypeHandler.class)
	private List<String> coordinate1;

	private String picture2;

	@TableField(typeHandler = ListToStringTypeHandler.class)
	private List<String> coordinate2;

	private String picture3;

	@TableField(typeHandler = ListToStringTypeHandler.class)
	private List<String> coordinate3;

	private String picture4;

	@TableField(typeHandler = ListToStringTypeHandler.class)
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
	 * 已有评论数
	 */
	private Integer commentNum;

	/**
	 * 平均图文匹配度
	 */
	private double matching;

	/**
	 * 综合评分
	 */
	private double total_score;

	/**
	 * 是否为低质量报告
	 */
	private String isBad;

	public FetchMission(FetchMissionVO fetchMission){
		BeanUtils.copyProperties(fetchMission, this);
	}

	public FetchMission(){
		setDislikes(0);
		setLikes(0);
		setScore(-1);
		setMatching(-1);
		setTotal_score(0);
		setCommentNum(0);
		setIsBad("False");
	}

}
