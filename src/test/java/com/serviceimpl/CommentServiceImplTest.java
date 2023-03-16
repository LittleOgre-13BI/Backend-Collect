package com.serviceimpl;

import com.mapperservice.CommentMapperService;
import com.mapperservice.UserMapperService;
import com.po.Comment;
import com.po.FetchMission;
import com.po.User;
import com.service.CommentService;
import com.service.FetchMissionService;
import com.utils.Response;
import com.vo.CommentVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@RunWith(SpringRunner.class)
@SpringBootTest
class CommentServiceImplTest {

    @MockBean
    private UserMapperService userMapperService;

    @MockBean
    private CommentMapperService commentMapperService;

    @MockBean
    private FetchMissionService fetchMissionService;

    @Autowired
    private CommentService commentService;

//    @BeforeEach
//    void setUp() {}

//    @AfterEach
//    void tearDown() {
//    }

    /**
     * 发布评价单元测试
     * 分支一：用户是否存在
     */
    @Test
    void submitComment() {
        // case 1 用户不存在
        long uid = 1L;
        CommentVO commentVO = new CommentVO();
        commentVO.setUid(uid);
        Mockito.doReturn(null).when(userMapperService).selectById(uid);
        assertEquals(500, commentService.submitComment(commentVO).get("code"));
        assertEquals("用户不存在", commentService.submitComment(commentVO).get("msg"));

        // case 2 成功发布评论
        uid = 2L;
        commentVO.setUid(uid);
        Mockito.doReturn(new User()).when(userMapperService).selectById(uid);
        Mockito.doReturn(new FetchMission()).when(fetchMissionService).getById(any());
        assertEquals(0, commentService.submitComment(commentVO).get("code"));
    }

    /**
     * 获取评论列表单元测试
     */
    @Test
    void getCommentList(){
        long fid = 1L;
        List<Comment> list = new ArrayList<>();
        Comment comment = new Comment();
        comment.setFid(fid);
        list.add(comment);
        Mockito.doReturn(list).when(commentMapperService).selectList(any());

        Response rslt = commentService.getCommentList(fid);
        assertEquals(0, rslt.get("code"));
        Object obj = rslt.get("data");
        List<CommentVO> result = new ArrayList<>();
        if (obj instanceof ArrayList<?>){
            for (Object o : (List<?>) obj){
                result.add((CommentVO) o);
            }
        }
        assertEquals(1, result.size());
        assertEquals(fid, result.get(0).getFid());
    }
}