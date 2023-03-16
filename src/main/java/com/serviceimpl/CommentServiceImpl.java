package com.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapperservice.CommentMapperService;
import com.mapperservice.UserMapperService;
import com.po.Comment;
import com.po.FetchMission;
import com.po.User;
import com.service.CommentService;
import com.service.FetchMissionService;
import com.utils.Response;
import com.vo.CommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapperService, Comment> implements CommentService {

    private final UserMapperService userMapperService;

    private final FetchMissionService fetchMissionService;

    @Autowired
    public CommentServiceImpl(UserMapperService userMapperService, FetchMissionService fetchMissionService){
        this.userMapperService = userMapperService;
        this.fetchMissionService = fetchMissionService;
    }

    /**
     * 发布评论，并根据评分计算报告平均评分
     * @param commentVO 评论
     * @return
     */
    @Override
    public Response submitComment(CommentVO commentVO) {
        User user = userMapperService.selectById(commentVO.getUid());
        if(user == null) return Response.error("用户不存在");

        commentVO.setNickname(user.getNickname());
        save(new Comment(commentVO));
        fetchMissionService.calScore(commentVO.getFid(),commentVO.getScore());
        return Response.ok();
    }

    /**
     * 获取接包任务下的评论列表
     * @param fid 接包任务fid
     * @return
     */
    @Override
    public Response getCommentList(Long fid) {
        List<CommentVO> commentVOList = new ArrayList<>();
        List<Comment> commentList = list(new QueryWrapper<Comment>().eq("fid",fid));
        for (Comment comment: commentList){
            commentVOList.add(new CommentVO(comment));
        }
        return Response.ok().put("data",commentVOList);
    }
}
