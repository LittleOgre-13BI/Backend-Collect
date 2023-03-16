package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.po.FetchMission;
import com.utils.Response;
import com.vo.FetchMissionVO;
import com.vo.MyPage;
import com.vo.ReportSimilarityVO;

import java.util.Date;
import java.util.List;

public interface FetchMissionService extends IService<FetchMission> {

    Response fetchMission(Long uid, Long mid, Date fetchDate);

    Response giveUpMission(Long uid, Long mid);

    Response submitReport(FetchMissionVO fetchMissionVO);

    MyPage<FetchMissionVO> searchReportsByMid(Long mid, Long pageIndex, Long pageSize, String mode);

    Response searchReportByIds(Long uid, Long mid);

    Response searchReportByFid(Long fid);

    MyPage<FetchMission> searchFetchMissionsByUid(Long uid, Long pageIndex, Long pageSize);

    Integer getMissionWorkersNum(Long mid);

    MyPage<FetchMissionVO> getBadReports(Long mid, Long pageIndex, Long pageSize);

//    List<FetchMissionVO> getSimilarReports(Long fid);

    MyPage<FetchMissionVO> getSimilarReports(Long fid, Long pageIndex, Long pageSize);

    List<ReportSimilarityVO> getSimilarityGraph(Long fid);

    void calScore(Long fid, Integer thisScore);


    Response amplificationReport(FetchMissionVO fetchMissionVO);

}

