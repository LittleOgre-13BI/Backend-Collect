package com.serviceimpl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mapperservice.ReportSimilarityMapperService;
import com.po.ReportSimilarity;
import com.service.AlgorithmService;
import com.service.FetchMissionService;
import com.service.ReportService;
import com.utils.Response;
import com.vo.AlgorithmFormVO;
import com.vo.FetchMissionVO;
import com.vo.ReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service("algorithmService")
public class AlgorithmServiceImpl implements AlgorithmService {

    @Value("${similarity-service.url}")
    private String server_url;

    @Autowired
    private ReportSimilarityMapperService reportSimilarityMapperService;
    @Override
    public void calReportSimilarity(Long fid, Long mid) {
        String url = server_url + "/similarity?fid={fid}&mid={mid}";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<similarityMap> response  = restTemplate.postForEntity(url, null, similarityMap.class, fid, mid);

        Assert.isTrue(HttpStatus.OK == response.getStatusCode(), "http call is failed");
        Iterator<Map.Entry<Long, Double>> it = response.getBody().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Long, Double> entry = it.next();
            Long fidA = entry.getKey();
            ReportSimilarity reportSimilarity = new ReportSimilarity();
            reportSimilarity.setFidA(fidA < fid ? fidA : fid);
            reportSimilarity.setFidB(fidA < fid ? fid : fidA);
            reportSimilarity.setSimilarity(entry.getValue());
            reportSimilarityMapperService.update(reportSimilarity, new QueryWrapper<ReportSimilarity>()
                    .and(i -> i.eq("fid_a", reportSimilarity.getFidA()).eq("fid_b", reportSimilarity.getFidB())));
            System.out.println(entry.getKey() + " : " + String.valueOf(entry.getValue()));
        }
    }

    @Override
    public double calMatching(Long fid) {
        String url = server_url + "/matching?fid={fid}";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, null, String.class, fid);
        Assert.isTrue(HttpStatus.OK == response.getStatusCode(), "http call is failed");
        return Double.parseDouble(Objects.requireNonNull(response.getBody()));
    }

    @Override
    public List<Long> recommendMissions(Long uid) {
        String url = server_url + "/recommend?uid={uid}";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<recommendMap> response  = restTemplate.postForEntity(url, null, recommendMap.class, uid);

        Assert.isTrue(HttpStatus.OK == response.getStatusCode(), "http call is failed");
        Iterator<Map.Entry<Long, Double>> it = response.getBody().entrySet().iterator();
        List<Long> rslt = new ArrayList<>();
        while (it.hasNext()){
            Map.Entry<Long, Double> entry = it.next();
            rslt.add(entry.getKey());
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        return rslt;
    }

    @Override
    public Response changeStrategy(AlgorithmFormVO algorithmFormVO) throws URISyntaxException{
        String url = server_url + "/setstrategy";
        RequestEntity<AlgorithmFormVO> request = RequestEntity
                .post(new URI(url))
                .accept(MediaType.APPLICATION_JSON)
                .body(algorithmFormVO);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);
        Assert.isTrue(HttpStatus.OK == response.getStatusCode(), "http call is failed");
        return Objects.equals(response.getBody(), algorithmFormVO.getRecommend()) ? Response.ok() : Response.error("更改推荐策略模式失败");
    }

    @Override
    public Response getStrategy() {
        String url = server_url + "/getstrategy";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AlgorithmFormVO> response = restTemplate.getForEntity(url, AlgorithmFormVO.class);

        Assert.isTrue(HttpStatus.OK == response.getStatusCode(), "http call is failed");
        if(response.getBody() == null) return Response.error("获取当前策略失败");
        return Response.ok().put("strategy", response.getBody());
    }

    @Override
    public Response amplificationReport(Response reportText) throws URISyntaxException {
        String url = server_url + "/amplification";
        RequestEntity<Response> request = RequestEntity
                .post(new URI(url))
                .accept(MediaType.APPLICATION_JSON)
                .body(reportText);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Response> response = restTemplate.exchange(request, Response.class);
        Assert.isTrue(HttpStatus.OK == response.getStatusCode(), "http call is failed");

        return response.getBody();
    }

    static class recommendMap extends HashMap<Long, Double>{ }

    static class similarityMap extends HashMap<Long, Double>{ }
}
