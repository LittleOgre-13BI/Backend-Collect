package com.vo;

import com.po.Comment;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class CommentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long cid;

    private Long fid;

    private Long uid;

    private String nickname;

    private Date submitTime;

    private String content;

    private Integer score;

    public CommentVO(Comment comment){
        BeanUtils.copyProperties(comment, this);
    }

    public CommentVO(){
        setScore(0);
    }
}
