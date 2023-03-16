package com.service;

import com.utils.Response;
import com.vo.AlgorithmFormVO;

import java.net.URISyntaxException;
import java.util.List;

public interface AlgorithmService {

    void calReportSimilarity(Long fid, Long mid);

    double calMatching(Long fid);

    List<Long> recommendMissions(Long uid);

    Response changeStrategy(AlgorithmFormVO algorithmFormVO) throws URISyntaxException;

    Response getStrategy();

    Response amplificationReport(Response reportText) throws URISyntaxException;

}
