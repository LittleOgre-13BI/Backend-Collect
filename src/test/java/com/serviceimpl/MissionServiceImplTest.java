package com.serviceimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mapperservice.FetchMissionMapperService;
import com.mapperservice.MissionMapperService;
import com.mapperservice.UserMapperService;
import com.po.FetchMission;
import com.po.Mission;
import com.po.User;
import com.service.AlgorithmService;
import com.service.MissionService;
import com.service.UserService;
import com.vo.MissionVO;
import com.vo.MyPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
class MissionServiceImplTest {

    @Autowired
    MissionService missionService;

    @MockBean
    FetchMissionMapperService fetchMissionMapperService;

    @MockBean
    UserMapperService userMapperService;

    @MockBean
    AlgorithmService algorithmService;

    @MockBean
    MissionMapperService missionMapperService;

//    @BeforeEach
//    void setUp() {
//    }
//
//    @AfterEach
//    void tearDown() {
//    }

//    /**
//     * 发包方发布测试任务单元测试
//     * 分支一：用户是否存在
//     * 分支二：用户是否权限发布测试任务
//     */
//    @Test
//    void addMission() {
//        long uid = 1L;
//        User user = new User();
//        MissionVO missionVO = new MissionVO();
//        missionVO.setUid(uid);
//        // case 1 用户不存在
//        Mockito.doReturn(null).when(userMapperService).selectById(uid);
//        assertEquals(500, missionService.addMission(missionVO).get("code"));
//        assertEquals("用户不存在", missionService.addMission(missionVO).get("msg"));
//
//        // case 2 用户没有权限发布测试任务
//        uid = 2L;
//        user.setUid(uid);
//        user.setUserType("employee");
//        missionVO.setUid(uid);
//        Mockito.doReturn(user).when(userMapperService).selectById(uid);
//        assertEquals(500, missionService.addMission(missionVO).get("code"));
//        assertEquals("众包工人不能发布测试任务", missionService.addMission(missionVO).get("msg"));
//
//        // case 3 发布成功
//        uid = 3L;
//        user.setUid(uid);
//        user.setUserType("employer");
//        missionVO.setUid(uid);
//        Mockito.doReturn(user).when(userMapperService).selectById(uid);
//        assertEquals(0, missionService.addMission(missionVO).get("code"));
//    }

    /**
     * 删除测试任务单元测试
     * 分支一：测试任务是否存在
     * 分支二：测试任务正在进行中不可删除
     */
    @Test
    void removeMission() {
        Mockito.doReturn(null).when(missionMapperService).selectById(1L);
        Date currDate = new Date();
        Mission mission = new Mission();
        mission.setLastFetchTime(currDate);
        mission.setTimeLimit(1);
        Mockito.doReturn(mission).when(missionMapperService).selectById(2L);

        // case 1 测试任务不存在
        assertEquals(500 ,missionService.removeMission(1L).get("code"));
        assertEquals("测试任务不存在" ,missionService.removeMission(1L).get("msg"));

        // case 2 测试任务正在进行中不可删除
        assertEquals(500 ,missionService.removeMission(2L).get("code"));
        assertEquals("测试任务正在进行中，不可删除，请在任务结束后再删除任务" ,missionService.removeMission(2L).get("msg"));

        // case 3 测试任务删除成功
        mission.setTimeLimit(0);
        Mockito.doReturn(mission).when(missionMapperService).selectById(3L);
        assertEquals(0 ,missionService.removeMission(3L).get("code"));
    }

    /**
     * 众包工人查询自己所有接过的测试任务单元测试
     */
    @Test
    void workerSearchMissionsByUid() {
        FetchMission fetchMission1 = new FetchMission();
        fetchMission1.setMid(1L);
        FetchMission fetchMission2 = new FetchMission();
        fetchMission2.setMid(2L);
        List<FetchMission> records = new ArrayList<FetchMission>(){{
            add(fetchMission1);
            add(fetchMission2);
        }};
        MyPage<FetchMission> fetchMissionList = new MyPage<>(records, 2L, 10L, 1L);
        Mission mission1 = new Mission();
        Mission mission2 = new Mission();
        mission1.setMid(1L);
        mission2.setMid(2L);
        Mockito.doReturn(mission1).when(missionMapperService).selectById(1L);
        Mockito.doReturn(mission2).when(missionMapperService).selectById(2L);

        MyPage<MissionVO> rslt = missionService.workerSearchMissionsByUid(fetchMissionList);

        // 检查分页数据
        assertEquals(1L, rslt.getCurrPage());
        assertEquals(10L, rslt.getPageSize());
        assertEquals(2L, rslt.getTotal());
        assertEquals(1L, rslt.getTotalPage());

        // 检查内容
        assertEquals(1L, rslt.getList().get(0).getMid());
        assertEquals(2L, rslt.getList().get(1).getMid());
    }

    /**
     * 发包方查询自己发布的测试任务
     */
    @Test
    void senderSearchMissionsByUid() {
        IPage<Mission> iPage = new Page<>(1L, 10L, 99L);
        Mockito.doReturn(iPage).when(missionMapperService).selectPage(any(), any());

        // 检查分页数据
        MyPage<MissionVO> rslt = missionService.senderSearchMissionsByUid(1L, 1L, 10L);
        assertEquals(1L, rslt.getCurrPage());
        assertEquals(10L, rslt.getPageSize());
        assertEquals(99L, rslt.getTotal());
        assertEquals(10L, rslt.getTotalPage());
    }

    /**
     * 查询所有测试任务单元测试
     */
    @Test
    void searchAllMissions() {
        long pageIndex = 1L;
        long pageSize = 10L;
        long total = 79L;
        IPage<Mission> iPage = new Page<>(pageIndex, pageSize, total);
        Mockito.doReturn(iPage).when(missionMapperService).selectPage(any(), any());

        // 检查分页数据
        MyPage<MissionVO> rslt = missionService.searchAllMissions();
        assertEquals(pageIndex, rslt.getCurrPage());
        assertEquals(pageSize, rslt.getPageSize());
        assertEquals(total, rslt.getTotal());
        assertEquals(total / pageSize + 1, rslt.getTotalPage());
    }

    /**
     * 根据mid查询测试任务单元测试
     * 分支一：测试任务是否存在
     */
    @Test
    void searchMissionByMid() {
        Mockito.doReturn(null).when(missionMapperService).selectById(1L);
        Mockito.doReturn(new Mission()).when(missionMapperService).selectById(2L);

        // case 1 测试任务不存在
        assertEquals(500, missionService.searchMissionByMid(1L).get("code"));
        assertEquals("测试任务不存在", missionService.searchMissionByMid(1L).get("msg"));

        // case 2 成功查询
        assertEquals(0, missionService.searchMissionByMid(2L).get("code"));
    }

    /**
     * 根据测试任务名称模糊查询单元测试
     */
    @Test
    void searchMissionsByMissionName() {
        long pageIndex = 1L;
        long pageSize = 10L;
        long total = 79L;
        IPage<Mission> iPage = new Page<>(pageIndex, pageSize, total);
        Mockito.doReturn(iPage).when(missionMapperService).selectPage(any(), any());

        // 检查分页数据
        MyPage<MissionVO> rslt = missionService.searchMissionsByMissionName("test", pageIndex, pageSize);
        assertEquals(pageIndex, rslt.getCurrPage());
        assertEquals(pageSize, rslt.getPageSize());
        assertEquals(total, rslt.getTotal());
        assertEquals(total / pageSize + 1, rslt.getTotalPage());
    }

    /**
     * 获取测试任务限时
     * 分支一：测试任务是否存在
     */
    @Test
    void getMissionTimeLimit() {
        Mockito.doReturn(null).when(missionMapperService).selectById(1L);
        Mission mission = new Mission();
        mission.setMid(2L);
        mission.setTimeLimit(360);
        Mockito.doReturn(mission).when(missionMapperService).selectById(2L);

        // case 1 测试任务不存在，返回任务时限为0
        assertEquals(0, missionService.getMissionTimeLimit(1L));


        // case 2 成功返回测试任务时限
        assertEquals(360, missionService.getMissionTimeLimit(2L));
    }

    /**
     * 查询测试任务是否存在单元测试
     */
    @Test
    void isMissionExist() {
        Mockito.doReturn(null).when(missionMapperService).selectById(1L);
        Mockito.doReturn(new Mission()).when(missionMapperService).selectById(2L);

        // case 1 测试任务不存在，返回任务时限为0
        assertFalse(missionService.isMissionExist(1L));

        // case 2 成功返回测试任务时限
        assertTrue(missionService.isMissionExist(2L));
    }

    /**
     * 为众包工人推荐任务单元测试
     */
    @Test
    void missionRecommendation() {
        long uid = 1L;
        long pageIndex = 1L;
        long pageSize = 10L;
        List<Long> recommend_mids = new ArrayList<Long>(){{
           add(1L);
           add(2L);
           add(3L);
        }};
        Mockito.doReturn(recommend_mids).when(algorithmService).recommendMissions(uid);
        Mission mission1 = new Mission();
        Mission mission2 = new Mission();
        Mission mission3 = new Mission();
        mission1.setMid(1L);
        mission2.setMid(2L);
        mission3.setMid(3L);
        Mockito.doReturn(mission1).when(missionMapperService).selectById(1L);
        Mockito.doReturn(mission2).when(missionMapperService).selectById(2L);
        Mockito.doReturn(mission3).when(missionMapperService).selectById(3L);

        // 检查分页数据
        MyPage<MissionVO> rslt = missionService.missionRecommendation(uid, pageIndex, pageSize);
        assertEquals(pageIndex, rslt.getCurrPage());
        assertEquals(pageSize, rslt.getPageSize());
        assertEquals(3L, rslt.getTotal());
        assertEquals( 1L, rslt.getTotalPage());

        // 检查分页内容
        assertEquals(1L, rslt.getList().get(0).getMid());
        assertEquals(2L, rslt.getList().get(1).getMid());
        assertEquals(3L, rslt.getList().get(2).getMid());
    }

    /**
     * 修改测试任务单元测试
     * 分支一：测试任务是否存在
     */
    @Test
    void saveMission() {
        Mockito.doReturn(null).when(missionMapperService).selectById(1L);
        Mockito.doReturn(new Mission()).when(missionMapperService).selectById(2L);

        // case 1 测试任务不存在
        MissionVO missionVO = new MissionVO();
        missionVO.setMid(1L);
        assertEquals(500, missionService.saveMission(missionVO).get("code"));
        assertEquals("测试任务不存在", missionService.saveMission(missionVO).get("msg"));
        // case 2 成功修改测试任务
        missionVO.setMid(2L);
        assertEquals(0, missionService.saveMission(missionVO).get("code"));
    }
}