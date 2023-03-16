package com.controller;

import com.mapperservice.FetchMissionMapperService;
import com.mapperservice.UserMapperService;
import com.service.AlgorithmService;
import com.service.FetchMissionService;
import com.service.ReportService;
import com.utils.Response;
import com.vo.AlgorithmFormVO;
import com.vo.FetchMissionVO;
import com.vo.ReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/algorithm")
@CrossOrigin(origins = "http://124.221.127.36", maxAge = 3600, allowCredentials="true",allowedHeaders = "*",methods = {RequestMethod.POST,RequestMethod.GET})
public class AlgorithmController {

    @Autowired
    AlgorithmService algorithmService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private FetchMissionService fetchMissionService;

    @PostMapping("/changestrategy")
    public Response changeStrategy(@RequestBody AlgorithmFormVO algorithmFormVO) {
        Response response;
        try {
            response = algorithmService.changeStrategy(algorithmFormVO);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return Response.error();
        }
        return response;
    }

    @GetMapping("/getstrategy")
    public Response getStrategy() {
        return algorithmService.getStrategy();
    }

    @PostMapping("/matching")
    public Response calMatching() {
        return Response.ok().put("matching", algorithmService.calMatching(2L));
    }

    /**
     * 查看扩增后的报告数据
     */
    @GetMapping("/amplification")
    public Response getReportAmplification(Long rid) throws URISyntaxException {
        Response reportText = new Response();
        ReportVO report = (ReportVO) reportService.getReport(rid).get("data");
        reportText.put("title", report.getTitle());
        reportText.put("bugDescription", report.getBugDescription());
        reportText.put("bugRecurrentSteps", report.getBugRecurrentSteps());
        reportText.put("deviceInformation", report.getDeviceInformation());
        return algorithmService.amplificationReport(reportText);
    }

    /**
     * 查看扩增后的报告数据
     */
    @GetMapping("/amplificationbyfid")
    public Response getReportAmplificationByFid(Long fid) throws URISyntaxException {
        Response reportText = new Response();
        FetchMissionVO report = (FetchMissionVO) fetchMissionService.searchReportByFid(fid).get("fetchmission");
        reportText.put("title", report.getTitle());
        reportText.put("bugDescription", report.getBugDescription());
        reportText.put("bugRecurrentSteps", report.getBugRecurrentSteps());
        reportText.put("deviceInformation", report.getDeviceInformation());
        return algorithmService.amplificationReport(reportText);
    }


}
