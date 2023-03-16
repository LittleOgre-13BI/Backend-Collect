package com.vo;

import com.po.Report;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class ReportVO implements Serializable {

    private Long rid;

    private Long fid;

    private Long uid;

    private String nickname;

    private Date submitTime;

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

    public ReportVO(Report report){
        BeanUtils.copyProperties(report,this);
    }

    public ReportVO(){

    }
    
}
