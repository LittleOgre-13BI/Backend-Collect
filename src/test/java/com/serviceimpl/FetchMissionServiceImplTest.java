package com.serviceimpl;

import com.SpringbootSchemaApplication;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mapperservice.FetchMissionMapperService;
import com.mapperservice.MissionMapperService;
import com.mapperservice.ReportSimilarityMapperService;
import com.po.FetchMission;
import com.po.Mission;
import com.po.ReportSimilarity;
import com.service.UserService;
import com.service.FetchMissionService;
import com.service.AlgorithmService;
import com.utils.Response;
import com.vo.FetchMissionVO;
import com.vo.MyPage;
import org.apache.ibatis.binding.MapperMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
class FetchMissionServiceImplTest {

    @Autowired
    private FetchMissionService fetchMissionService;

    @Autowired
    private FetchMissionServiceImpl fetchMissionServiceImpl;

    @MockBean
    private MissionMapperService missionMapperService;

    @MockBean
    private ReportSimilarityMapperService reportSimilarityMapperService;

    @MockBean
    private FetchMissionMapperService fetchMissionMapperService;

    @MockBean
    private UserService userService;

    @MockBean
    private AlgorithmService algorithmService;


//    @BeforeEach
//    void setUp() { }
//
//    @AfterEach
//    void tearDown() {}

    /**
     * 接包单元测试
     * 分支一：测试任务是否存在
     * 分支二：测试任务是否结束招募
     * 分支三：接包任务是否存在
     */
    @Test
    void fetchMission() {
        Date fetchDate = new Date();
        Mission mission = new Mission();

        // case 1 测试任务不存在
        Mockito.doReturn(null).when(missionMapperService).selectById(1L);
        assertEquals(500, fetchMissionService.fetchMission(1L, 1L, fetchDate).get("code"));
        assertEquals("测试任务不存在", fetchMissionService.fetchMission(1L, 1L, fetchDate).get("msg"));
        // case 2 测试任务结束招募
        mission.setState("Ongoing");
        Mockito.doReturn(mission).when(missionMapperService).selectById(2L);
        assertEquals(500 , fetchMissionService.fetchMission(1L, 2L, fetchDate).get("code"));
        assertEquals("测试任务已结束招募", fetchMissionService.fetchMission(1L, 2L, fetchDate).get("msg"));
        // case 3 接包任务已存在
        mission.setState("Recruiting");
        Mockito.doReturn(mission).when(missionMapperService).selectById(3L);
        Mockito.doReturn(new FetchMission(), (Object) null).when(fetchMissionMapperService).selectOne(any());
        Response case3 = fetchMissionService.fetchMission(1L, 3L, fetchDate);
        assertEquals(500, case3.get("code"));
        assertEquals("接包任务已存在", case3.get("msg"));

        // case 4 成功接包
        Mockito.doReturn(mission).when(missionMapperService).selectById(4L);
        assertEquals(0, fetchMissionService.fetchMission(1L, 4L, fetchDate).get("code"));

        //        private方法测试
//        Class c = FetchMissionServiceImpl.class;
//        Method method = c.getDeclaredMethod("searchFetchMission", Long.class, Long.class);
//        method.setAccessible(true);
//        method.invoke(fetchMissionService, 1L, 3L);
    }


    /**
     * 放弃任务单元测试
     * 分支一：测试任务是否存在
     * 分支二：测试任务是否已结束
     * 分支三：接包任务是否存在
     */
    @Test
    void giveUpMission(){
        // case 1 测试任务不存在
        Mockito.doReturn(null).when(missionMapperService).selectById(1L);
        assertEquals(500, fetchMissionService.giveUpMission(1L, 1L).get("code"));
        assertEquals("测试任务不存在", fetchMissionService.giveUpMission(1L, 1L).get("msg"));

        // case 2 测试任务已结束
        Mission mission = new Mission();
        mission.setState("Done");
        Mockito.doReturn(mission).when(missionMapperService).selectById(2L);
        assertEquals(500 , fetchMissionService.giveUpMission(1L, 2L).get("code"));
        assertEquals("测试任务已结束", fetchMissionService.giveUpMission(1L, 2L).get("msg"));

        // case 3 接包任务不存在
        mission.setState("Recruiting");
        Mockito.doReturn(mission).when(missionMapperService).selectById(3L);
        Mockito.doReturn(null, new FetchMission()).when(fetchMissionMapperService).selectOne(any());
        Response case3 = fetchMissionService.giveUpMission(1L, 3L);
        assertEquals(500, case3.get("code"));
        assertEquals("接包任务不存在", case3.get("msg"));

        // case 4 放弃成功
        Mockito.doReturn(mission).when(missionMapperService).selectById(4L);
        assertEquals(0, fetchMissionService.giveUpMission(1L, 4L).get("code"));
    }

    /**
     * 提交报告单元测试
     * 分支一：测试任务是否存在
     * 分支二：任务是否已结束
     * 分支三：接包任务是否存在
     * 分支四：报告提交时间是否已超时
     */
    @Test
    void submitReport() {
        // case 1 测试任务不存在
        long mid = 1L;
        FetchMissionVO fetchMissionVO = new FetchMissionVO();
        fetchMissionVO.setMid(mid);
        Mockito.doReturn(null).when(missionMapperService).selectById(mid);
        assertEquals(500, fetchMissionService.submitReport(fetchMissionVO).get("code"));
        assertEquals("测试任务不存在", fetchMissionService.submitReport(fetchMissionVO).get("msg"));

        // case 2 测试任务已结束
        mid = 2L;
        fetchMissionVO.setMid(mid);
        Mission mission = new Mission();
        mission.setState("Done");
        Mockito.doReturn(mission).when(missionMapperService).selectById(mid);
        assertEquals(500, fetchMissionService.submitReport(fetchMissionVO).get("code"));
        assertEquals("测试任务已结束", fetchMissionService.submitReport(fetchMissionVO).get("msg"));

        // case 3 接包任务不存在
        mid = 3L;
        Date currDate = new Date();
        fetchMissionVO.setMid(mid);
        fetchMissionVO.setSubmitTime(currDate);
        mission.setState("Recruiting");
        FetchMission fetchMission4 = new FetchMission();
        FetchMission fetchMission5 = new FetchMission();
        fetchMission4.setFetchDate(currDate);
        fetchMission5.setFetchDate(currDate);
        fetchMission4.setTimeLimit(-1);
        fetchMission5.setTimeLimit(1);
        Mockito.doReturn(mission).when(missionMapperService).selectById(mid);
        Mockito.doReturn(null, fetchMission4, fetchMission5).when(fetchMissionMapperService).selectOne(any());
        Response case3 = fetchMissionService.submitReport(fetchMissionVO);
        assertEquals(500, case3.get("code"));
        assertEquals("接包任务不存在", case3.get("msg"));

        // case 4 提交已超过任务时限
        mid = 4L;
        fetchMissionVO.setMid(mid);
        Mockito.doReturn(mission).when(missionMapperService).selectById(mid);
        Response case4 = fetchMissionService.submitReport(fetchMissionVO);
        assertEquals(500, case4.get("code"));
        assertEquals("提交已超过任务时限", case4.get("msg"));

        // case 5 成功提交报告
        mid = 5L;
        fetchMissionVO.setMid(mid);
        Mockito.doReturn(mission).when(missionMapperService).selectById(mid);
        assertEquals(0, fetchMissionService.submitReport(fetchMissionVO).get("code"));
    }


    /**
     *  查询测试任务下所有报告单元测试
     *  分支一：检查mode参数格式是否正确
     */
    @Test
    void searchReportsByMid() {
        long pageIndex = 1L;
        long pageSize = 10L;
        long total = 79L;
        String mode = "TNT";
        IPage<Mission> iPage = new Page<>(pageIndex, pageSize, total);
        Mockito.doReturn(iPage).when(fetchMissionMapperService).selectPage(any(), any());

        // case 1 mode 格式不正确，返回空页
        MyPage<FetchMissionVO> rslt = fetchMissionService.searchReportsByMid(1L, pageIndex, pageSize, mode);
        assertEquals(0, rslt.getTotal());
        assertEquals(pageIndex, rslt.getCurrPage());
        assertEquals(pageSize, rslt.getPageSize());
        assertEquals(0, rslt.getTotalPage());

        // case 2 mode 格式正确，检查MyPage分页数据
        mode = "totalScoreDesc";
        rslt = fetchMissionService.searchReportsByMid(1L, pageIndex, pageSize, mode);
        assertEquals(total, rslt.getTotal());
        assertEquals(pageIndex, rslt.getCurrPage());
        assertEquals(pageSize, rslt.getPageSize());
        assertEquals(total / pageSize + 1, rslt.getTotalPage());
    }

    /**
     *  根据uid和mid查询接包任务单元测试
     *  分支一：接包任务是否存在
     */
    @Test
    void searchReportByIds() {
        Mockito.doReturn(null, new FetchMission()).when(fetchMissionMapperService).selectOne(any());

        // case 1 接包任务不存在
        Response case1 = fetchMissionService.searchReportByIds(1L, 1L);
        assertEquals(500, case1.get("code"));
        assertEquals("接包任务不存在", case1.get("msg"));

        // case 2 查询成功
        assertEquals(0, fetchMissionService.searchReportByIds(1L, 2L).get("code"));
    }

    /**
     * 根据fid查询接包任务单元测试
     * 分支一：接包任务是否存在
     */
    @Test
    void searchReportByFid(){
        Mockito.doReturn(null).when(fetchMissionMapperService).selectById(1L);
        Mockito.doReturn(new FetchMission()).when(fetchMissionMapperService).selectById(2L);

        // case 1 接包任务不存在
        Response case1 = fetchMissionService.searchReportByFid(1L);
        assertEquals(500, case1.get("code"));
        assertEquals("接包任务不存在", case1.get("msg"));

        // case 2 查询成功
        assertEquals(0, fetchMissionService.searchReportByFid(2L).get("code"));
    }

    /**
     * 众包工人查询自己的接包任务单元测试
     *
     */
    @Test
    void searchFetchMissionsByUid() {
        long pageIndex = 1L;
        long pageSize = 10L;
        long total = 55L;
        IPage<Mission> iPage = new Page<>(pageIndex, pageSize, total);
        Mockito.doReturn(iPage).when(fetchMissionMapperService).selectPage(any(), any());

        // 检查MyPage分页数据
        MyPage<FetchMission> rslt = fetchMissionService.searchFetchMissionsByUid(1L, pageIndex, pageSize);
        assertEquals(total, rslt.getTotal());
        assertEquals(pageIndex, rslt.getCurrPage());
        assertEquals(pageSize, rslt.getPageSize());
        assertEquals(total / pageSize + 1, rslt.getTotalPage());
    }

    /**
     * 查询测试任务下的所有低质量报告
     */
    @Test
    void getBadReports() {
        long pageIndex = 1L;
        long pageSize = 10L;
        long total = 55L;
        IPage<Mission> iPage = new Page<>(pageIndex, pageSize, total);
        Mockito.doReturn(iPage).when(fetchMissionMapperService).selectPage(any(), any());

        // 检查MyPage分页数据
        MyPage<FetchMissionVO> rslt = fetchMissionService.getBadReports(1L, pageIndex, pageSize);
        assertEquals(total, rslt.getTotal());
        assertEquals(pageIndex, rslt.getCurrPage());
        assertEquals(pageSize, rslt.getPageSize());
        assertEquals(total / pageSize + 1, rslt.getTotalPage());
    }

    /**
     * 众包工人提交报告后查询测试任务下与该报告最相似的三个报告
     *
     * case1:测试任务下无其他报告
     * case2:测试任务下的报告数大于3,方法仅返回相似度前三个
     */
    @Test
    void getSimilarReports() {
        // case 1 测试任务下无其他报告
        long fid = 1L;
        long pageIndex = 1L;
        long pageSize= 10L;
        List<ReportSimilarity> list = new ArrayList<>();
        Mockito.doReturn(list).when(reportSimilarityMapperService).selectList(any());
        MyPage<FetchMissionVO> case1 = fetchMissionService.getSimilarReports(fid, pageIndex, pageSize);
        assertEquals(0, case1.getTotal());
        assertEquals(pageIndex, case1.getCurrPage());
        assertEquals(pageSize, case1.getPageSize());
        assertEquals(0, case1.getTotalPage());

        // case 2 测试任务下的报告数大于3,方法仅返回相似度前三个
        fid = 5L;
        ReportSimilarity r1 = new ReportSimilarity();
        ReportSimilarity r2 = new ReportSimilarity();
        ReportSimilarity r3 = new ReportSimilarity();
        ReportSimilarity r4 = new ReportSimilarity();
        r1.setFidA(1L);
        r2.setFidA(2L);
        r3.setFidA(3L);
        r4.setFidA(4L);
        r1.setSimilarity(0.1);
        r2.setSimilarity(0.4);
        r3.setSimilarity(0.0);
        r4.setSimilarity(0.2);
        r1.setFidB(fid);
        r2.setFidB(fid);
        r3.setFidB(fid);
        r4.setFidB(fid);
        list.add(r1);
        list.add(r2);
        list.add(r3);
        list.add(r4);
        Mockito.doReturn(list).when(reportSimilarityMapperService).selectList(any());
        FetchMission fetchMission1 = new FetchMission();
        FetchMission fetchMission2 = new FetchMission();
        FetchMission fetchMission3 = new FetchMission();
        FetchMission fetchMission4 = new FetchMission();
        fetchMission1.setFid(1L);
        Mockito.doReturn(fetchMission1).when(fetchMissionMapperService).selectById(1L);
        fetchMission2.setFid(2L);
        Mockito.doReturn(fetchMission2).when(fetchMissionMapperService).selectById(2L);
        fetchMission3.setFid(3L);
        Mockito.doReturn(fetchMission3).when(fetchMissionMapperService).selectById(3L);
        fetchMission4.setFid(4L);
        Mockito.doReturn(fetchMission4).when(fetchMissionMapperService).selectById(4L);

        MyPage<FetchMissionVO> case2 = fetchMissionService.getSimilarReports(fid, pageIndex, pageSize);
        // 检查分页数据
        assertEquals(3L, case2.getTotal());
        assertEquals(pageIndex, case2.getCurrPage());
        assertEquals(pageSize, case2.getPageSize());
        assertEquals(1L, case2.getTotalPage());

        // 检查排序内容
        assertEquals(2L, case2.getList().get(0).getFid());
        assertEquals(4L, case2.getList().get(1).getFid());
        assertEquals(1L, case2.getList().get(2).getFid());
    }

    /**
     * 返回该接包任务的报告与在其测试任务下的所有报告的相似度
     *
     * case1:测试任务下无其他报告
     * case2:返回该报告与所在测试任务下所有报告的相似度
     */
    @Test
    void getSimilarityGraph() {
        // case 1 测试任务下无其他报告
        long fid = 1L;
        List<ReportSimilarity> list = new ArrayList<>();
        Mockito.doReturn(list).when(reportSimilarityMapperService).selectList(any());
        assertEquals(0, fetchMissionService.getSimilarityGraph(fid).size());

        // case 2 返回测试任务下其余的四分报告
        fid = 2L;
        list.add(new ReportSimilarity());
        list.add(new ReportSimilarity());
        list.add(new ReportSimilarity());
        list.add(new ReportSimilarity());
        Mockito.doReturn(list).when(reportSimilarityMapperService).selectList(any());
        Mockito.doReturn(new FetchMission()).when(fetchMissionMapperService).selectById(any());
        assertEquals(4, fetchMissionService.getSimilarityGraph(fid).size());
    }

    /**
     * 查询测试任务下的已接包数单元测试
     * case1:没有该测试任务或该测试任务下没有接包
     * case2:测试任务下的接包数
     */
    @Test
    void getMissionWorkersNum() {
        // case 1 没有该测试任务或该测试任务下没有接包
        Mockito.doReturn(0).when(fetchMissionMapperService).selectCount(any());
        assertEquals(0, fetchMissionService.getMissionWorkersNum(1L));

        // case 2 该测试任务下有5个众包工人接包
        Mockito.doReturn(5).when(fetchMissionMapperService).selectCount(any());
        assertEquals(5, fetchMissionService.getMissionWorkersNum(2L));
    }

    /**
     * 评论后以及提交报告后重新计算综合评分
     * 对score、matching、picture1三个变量检查共有五个分支
     */
    @Test
    void CalTotalScore() throws Exception {
        FetchMission fetchMission = new FetchMission();
        double neScore = -1.0;
        double Score = 1.0;
        double neMatching = neScore;
        double Matching = Score;
        String picture1 = "AAA";

        // case 1 没有评论且报告已提交而图文匹配度未结算, 综合评分两部分都赋予随机值
        fetchMission.setScore(neScore);
        fetchMission.setMatching(neMatching);
        fetchMission.setPicture1(picture1);
        assertNotEquals(fetchMission.getScore() + fetchMission.getMatching(), fetchMissionServiceImpl.CalTotalScore(fetchMission));

        // case 2 有评论但报告已提交而图文匹配度未结算, 综合评分图文匹配部分赋予随机值
        fetchMission.setScore(Score);
        assertNotEquals(fetchMission.getScore() + fetchMission.getMatching(), fetchMissionServiceImpl.CalTotalScore(fetchMission));

        // case 3 有评论图文匹配度已结算，综合评分等于评论均分加上图文匹配度
        fetchMission.setMatching(Matching);
        assertEquals(fetchMission.getScore() + fetchMission.getMatching(), fetchMissionServiceImpl.CalTotalScore(fetchMission));

        // case 4 图文匹配度已结算但无评论，综合评分评论部分赋予随机值
        fetchMission.setScore(neScore);
        assertNotEquals(fetchMission.getScore() + fetchMission.getMatching(), fetchMissionServiceImpl.CalTotalScore(fetchMission));

        // case 5 无评论且报告未提交，综合评分评论部分赋予随机值，图文匹配部分为零
        fetchMission.setPicture1(null);
        fetchMission.setMatching(neMatching);
        assertNotEquals(fetchMission.getScore(), fetchMissionServiceImpl.CalTotalScore(fetchMission));


        // case 6 有评论但报告未提交，综合图文匹配部分为零
        fetchMission.setScore(Score);
        assertEquals(fetchMission.getScore(), fetchMissionServiceImpl.CalTotalScore(fetchMission));
    }

    /**
     * 判断接包任务所提交的报告质量
     */
    @Test
    void JudgeIsBad(){
        FetchMission fetchMission = new FetchMission();

        // case 1 报告无评论，质量待评判
        double score = -1.0;
        fetchMission.setScore(score);
        assertEquals("Judging", fetchMissionServiceImpl.JudgeIsBad(fetchMission));

        // case 2 报告有评论，但图文报告未结算，质量待评判
        double matching = -1.0;
        String picture1 = "AAA";
        fetchMission.setMatching(matching);
        fetchMission.setPicture1(picture1);
        assertEquals("Judging", fetchMissionServiceImpl.JudgeIsBad(fetchMission));

        // case 3 报告评论均分低于等于2.0或图文匹配度低于0.5，判断报告为低质量报告
        score = 1.0;
        matching = 1.0;
        fetchMission.setScore(score);
        fetchMission.setMatching(matching);
        assertEquals("True", fetchMissionServiceImpl.JudgeIsBad(fetchMission));

        // case 4 报告评分高于2.0，图文匹配度高于0.5，判断报告不是低质量报告
        score = 3.0;
        fetchMission.setScore(score);
        assertEquals("False", fetchMissionServiceImpl.JudgeIsBad(fetchMission));
    }
}