package com.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.vo.CommentVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("comment")
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评论编号
     */
    @TableId
    private Long cid;

    /**
     * 主报告编号
     */
    private Long fid;

    /**
     * 用户编号
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
     * 评论内容
     */
    private String content;

    /**
     * 量化评分
     */
    private Integer score;

    public Comment(CommentVO commentVO){
        BeanUtils.copyProperties(commentVO,this);
    }

    public Comment(){
        setScore(0);
    }
}
