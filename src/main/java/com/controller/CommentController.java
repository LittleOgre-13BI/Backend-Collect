package com.controller;

import com.service.CommentService;
import com.utils.Response;
import com.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://124.221.127.36", maxAge = 3600, allowCredentials="true",allowedHeaders = "*",methods = {RequestMethod.POST,RequestMethod.GET})
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 发布评价
     */
    @PostMapping("submitcomment")
    public Response submitComment(Long fid, Long uid, @RequestBody CommentVO commentVO){
        commentVO.setFid(fid);
        commentVO.setUid(uid);
        return commentService.submitComment(commentVO);
    }

    /**
     * 获得某一主报告的评论列表
     */
    @GetMapping("getcommentlist")
    public Response getCommentList(Long fid){
        return commentService.getCommentList(fid);
    }
}
