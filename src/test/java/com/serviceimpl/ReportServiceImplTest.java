package com.serviceimpl;

import com.mapperservice.FetchMissionMapperService;
import com.mapperservice.ReportMapperService;
import com.mapperservice.UserMapperService;
import com.po.FetchMission;
import com.po.Report;
import com.po.User;
import com.service.ReportService;
import com.utils.Response;
import com.vo.ReportVO;
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
class ReportServiceImplTest {

    @Autowired
    ReportService reportService;

    @MockBean
    UserMapperService userMapperService;

    @MockBean
    ReportMapperService reportMapperService;

    @MockBean
    FetchMissionMapperService fetchMissionMapperService;

    /**
     * 提交协作报告单元测试
     * 分支一：用户是否存在
     * 分支二：所协作的报告是否存在
     */
    @Test
    void submitReport() {

        // case 1 用户不存在
        long uid = 1L;
        ReportVO reportVO = new ReportVO();
        reportVO.setUid(uid);
        Mockito.doReturn(null).when(userMapperService).selectById(uid);
        assertEquals(500, reportService.submitReport(reportVO).get("code"));
        assertEquals("用户不存在", reportService.submitReport(reportVO).get("msg"));

        // case 2 所协作的主报告不存在
        uid = 2L;
        long fid = 1L;
        User user = new User();
        reportVO.setUid(uid);
        reportVO.setFid(fid);
        Mockito.doReturn(user).when(userMapperService).selectById(uid);
        Mockito.doReturn(null).when(fetchMissionMapperService).selectById(fid);
        assertEquals(500, reportService.submitReport(reportVO).get("code"));
        assertEquals("所协作的主报告不存在", reportService.submitReport(reportVO).get("msg"));

        // case 3 提交协作报告成功
        uid = 3L;
        fid = 2L;
        reportVO.setUid(uid);
        reportVO.setFid(fid);
        Mockito.doReturn(user).when(userMapperService).selectById(uid);
        Mockito.doReturn(new FetchMission()).when(fetchMissionMapperService).selectById(fid);
        assertEquals(0, reportService.submitReport(reportVO).get("code"));
    }

    /**
     * 获取接包任务的所有协作报告单元测试
     */
    @Test
    void getReportList() {
        long fid = 1L;
        List<Report> reportList = new ArrayList<>();
        Report report = new Report();
        report.setFid(fid);
        reportList.add(report);
        Mockito.doReturn(reportList).when(reportMapperService).selectList(any());

        Response rslt = reportService.getReportList(fid);
        assertEquals(0, rslt.get("code"));
        Object obj = rslt.get("data");
        List<ReportVO> result = new ArrayList<>();
        if(obj instanceof List<?>){
            for (Object o : (List<?>) obj){
                result.add((ReportVO) o);
            }
        }
        assertEquals(1, result.size());
        assertEquals(fid, result.get(0).getFid());
    }

    /**
     * 获取协作报告单元测试
     * 分支一：协作报告不存在
     */
    @Test
    void getReport() {
        long rid = 1L;
        // case 1 协作报告不存在
        Mockito.doReturn(null).when(reportMapperService).selectById(rid);
        assertEquals(500, reportService.getReport(rid).get("code"));
        assertEquals("协作报告不存在", reportService.getReport(rid).get("msg"));

        // case 2 获取成功
        rid = 2L;
        Mockito.doReturn(new Report()).when(reportMapperService).selectById(rid);
        assertEquals(0, reportService.getReport(rid).get("code"));
    }

}