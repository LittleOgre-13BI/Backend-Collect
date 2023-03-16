package com.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.IService;
import com.po.Report;
import com.utils.Response;
import com.vo.FetchMissionVO;
import com.vo.MyPage;
import com.vo.ReportVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public interface ReportService extends IService<Report> {
    Response submitReport(ReportVO reportVO);

    Response getReportList(Long fid);

    Response getReport(Long rid);
}
