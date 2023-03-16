package com.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapperservice.FetchMissionMapperService;
import com.mapperservice.ReportMapperService;
import com.mapperservice.UserMapperService;
import com.po.FetchMission;
import com.po.Report;
import com.po.User;
import com.service.ReportService;
import com.service.UserService;
import com.utils.PageHelper;
import com.utils.Response;
import com.vo.MyPage;
import com.vo.ReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("reportService")
public class ReportServiceImpl extends ServiceImpl<ReportMapperService, Report> implements ReportService {

    @Autowired
    private UserMapperService userMapperService;

    @Autowired
    private FetchMissionMapperService fetchMissionMapperService;

    /**
     * 提交协作报告
     * @param reportVO 协作报告详细信息
     * @return
     */
    @Override
    public Response submitReport(ReportVO reportVO) {
        User user = userMapperService.selectById(reportVO.getUid());
        if(user == null) return Response.error("用户不存在");
        FetchMission fetchMission = fetchMissionMapperService.selectById(reportVO.getFid());
        if(fetchMission == null) return Response.error("所协作的主报告不存在");

        reportVO.setNickname(user.getNickname());
        save(new Report(reportVO));
        return Response.ok();
    }

    /**
     * 获取接包任务的所有协作报告
     * @param fid 接包任务fid
     * @return
     */
    @Override
    public Response getReportList(Long fid) {
        List<Report> reportList = list(new QueryWrapper<Report>().eq("fid", fid));
        List<ReportVO> reportVOList = new ArrayList<>();
        for (Report report: reportList){
            reportVOList.add(new ReportVO(report));
        }
        return Response.ok().put("data",reportVOList);
    }

    /**
     * 根据rid获取协作报告
     * @param rid 协作报告rid
     * @return
     */
    @Override
    public Response getReport(Long rid) {
        Report report = getById(rid);
        if(report == null) return Response.error("协作报告不存在");
        return Response.ok().put("data", new ReportVO(report));
    }
}
