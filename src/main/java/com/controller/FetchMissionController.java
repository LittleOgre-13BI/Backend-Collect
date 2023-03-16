package com.controller;

import java.util.*;

import com.service.FetchMissionService;
import com.vo.FetchMissionVO;
import com.vo.MyPage;
import com.vo.ReportSimilarityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.utils.Response;

@RestController
@CrossOrigin(origins = "http://124.221.127.36", maxAge = 3600, allowCredentials="true",allowedHeaders = "*",methods = {RequestMethod.POST,RequestMethod.GET})
@RequestMapping("/fetchmission")
public class FetchMissionController {

    @Autowired
    private FetchMissionService fetchMissionService;

    /**
     * 接包
     */
    @PostMapping("/fetchmission")
	public Response fetchMission(Long uid, Long mid, @RequestBody FetchMissionVO fetchMissionVO){
		Date fetchDate = fetchMissionVO.getFetchDate();
		return fetchMissionService.fetchMission(uid, mid, fetchDate);
	}

	/**
	 * 放弃任务
	 */
	@PostMapping("/giveupmission")
	public Response giveUpMission(Long uid, Long mid){
		return fetchMissionService.giveUpMission(uid, mid);
	}

    /**
     * 提交报告
     */
    @PostMapping( "/submitreport")
	public Response submitReport(Long uid, Long mid, @RequestBody FetchMissionVO fetchMissionVO){
		fetchMissionVO.setUid(uid);
		fetchMissionVO.setMid(mid);
		return fetchMissionService.submitReport(fetchMissionVO);
	}

	/**
     * 查找某一任务下的所有报告
     */
    @GetMapping("/searchreportsbymid")
	public MyPage<FetchMissionVO> searchReportsByMid(Long mid, long pageIndex, long pageSize, String mode){
		return fetchMissionService.searchReportsByMid(mid, pageIndex, pageSize, mode);
	}

	/**
	 * 根据uid和mid查找某一份报告
	 */
	@GetMapping("/searchreportbyids")
	public Response searchReportByIds(Long uid, Long mid){
		return fetchMissionService.searchReportByIds(uid, mid);
	}


	/**
	 * 根据fid查找接包任务
	 */
	@GetMapping("/searchreportbyfid")
	public Response searchReportByFid(Long fid){ return fetchMissionService.searchReportByFid(fid);}

	/**
	 * 报告提交后，为众包工人返回相似度高的已有报告，引导报告协作
	 */
	@GetMapping("/getsimilarreports")
	public MyPage<FetchMissionVO> getSimilarReports(Long fid, Long pageIndex, Long pageSize){
		return fetchMissionService.getSimilarReports(fid, pageIndex, pageSize);
	}


	@GetMapping("/getsimilaritygraph")
	public List<ReportSimilarityVO> getSimilarityGraph(Long fid){
		return fetchMissionService.getSimilarityGraph(fid);
	}

	/**
	 * 查找当前任务下的所有低质量报告
	 */
	@GetMapping("getbadreports")
	public MyPage<FetchMissionVO> getBadReports(Long mid, Long pageIndex, Long pageSize){
		return fetchMissionService.getBadReports(mid, pageIndex, pageSize);
	}

	/**
	 * 扩增报告
	 */
	@PostMapping( "/amplificationreport")
	public Response amplificationReport(Long uid, Long mid, @RequestBody FetchMissionVO fetchMissionVO){
		fetchMissionVO.setUid(uid);
		fetchMissionVO.setMid(mid);
		return fetchMissionService.amplificationReport(fetchMissionVO);
	}

}
