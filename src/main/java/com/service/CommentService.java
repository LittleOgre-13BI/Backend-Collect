package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.po.Comment;
import com.utils.Response;
import com.vo.CommentVO;

public interface CommentService extends IService<Comment> {
    Response submitComment(CommentVO commentVO);

    Response getCommentList(Long fid);
}
