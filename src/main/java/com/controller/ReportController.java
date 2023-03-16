package com.controller;

import com.service.ReportService;
import com.utils.Response;
import com.vo.MyPage;
import com.vo.ReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://124.221.127.36", maxAge = 3600, allowCredentials="true",allowedHeaders = "*",methods = {RequestMethod.POST,RequestMethod.GET})
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 提交补充说明报告
     */
    @PostMapping("submitreport")
    public Response submitReport(Long fid, Long uid, @RequestBody ReportVO reportVO){
        reportVO.setFid(fid);
        reportVO.setUid(uid);
        return reportService.submitReport(reportVO);
    }

    /**
     * 查看某一主报告的补充说明报告列表
     */
    @GetMapping("getreportlist")
    public Response getReportList(Long fid){
        return reportService.getReportList(fid);
    }

    /**
     * 查看补充说明报告详情
     */
    @GetMapping("getreport")
    public Response getReport(Long rid){
        return reportService.getReport(rid);
    }



}
