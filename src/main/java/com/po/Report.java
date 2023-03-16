package com.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.vo.ReportVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("report")
public class Report implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务编号
     */
    @TableId
    private Long rid;

    /**
     * 主报告编号
     */
    private Long fid;

    /**
     * 发布该补充说明报告的用户编号
     */
    private Long uid;

    /**
     * 用户昵称
     */
    private String nickname;

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

    private String picture2;

    private String picture3;

    private String picture4;

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

    public Report(ReportVO reportVO){
        BeanUtils.copyProperties(reportVO, this);
    }

    public Report(){

    }
}
