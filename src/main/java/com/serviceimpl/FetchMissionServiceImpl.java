package com.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mapperservice.MissionMapperService;
import com.mapperservice.ReportSimilarityMapperService;
import com.po.Mission;
import com.po.ReportSimilarity;
import com.service.AlgorithmService;
import com.service.UserService;
import com.utils.PageHelper;
import com.utils.Response;
import com.vo.FetchMissionVO;
import com.vo.MyPage;
import com.vo.ReportSimilarityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mapperservice.FetchMissionMapperService;
import com.po.FetchMission;
import com.service.FetchMissionService;

@Service("fetchMissionService")
public class FetchMissionServiceImpl extends ServiceImpl<FetchMissionMapperService, FetchMission> implements FetchMissionService {

    private final MissionMapperService missionMapperService;

    private final ReportSimilarityMapperService reportSimilarityMapperService;

    private final UserService userService;

    private final AlgorithmService algorithmService;

    @Autowired
    public FetchMissionServiceImpl(MissionMapperService missionMapperService,
                        ReportSimilarityMapperService reportSimilarityMapperService,
                        UserService userService,
                        AlgorithmService algorithmService){
        this.missionMapperService = missionMapperService;
        this.reportSimilarityMapperService = reportSimilarityMapperService;
        this.userService = userService;
        this.algorithmService = algorithmService;
    }

    /**
     * 众包工人接包
     *
     * @param uid 众包工人uid
     * @param mid 测试任务mid
     * @param fetchDate 接包时间
     * @return
     */
    @Override
    public Response fetchMission(Long uid, Long mid, Date fetchDate) {
        Mission mission = missionMapperService.selectById(mid);
        if(mission == null) return Response.error("测试任务不存在");
        if(!mission.getState().equals("Recruiting")) return Response.error("测试任务已结束招募");
        if(searchFetchMission(uid, mid) != null) return Response.error("接包任务已存在");

        mission.setLastFetchTime(fetchDate);
        missionMapperService.updateById(mission);
        FetchMission fetchMission = new FetchMission();
        fetchMission.setUid(uid);
        fetchMission.setMid(mid);
        fetchMission.setFetchDate(fetchDate);
        fetchMission.setTimeLimit(mission.getTimeLimit());
        save(fetchMission);
        return Response.ok();
    }

    /**
     * 众包工人放弃任务
     * @param uid 众包工人uid
     * @param mid 测试任务mid
     * @return
     */
    @Override
    public Response giveUpMission(Long uid, Long mid) {
        Mission mission = missionMapperService.selectById(mid);
        if(mission == null) return Response.error("测试任务不存在");
        if(mission.getState().equals("Done")) return Response.error("测试任务已结束");
        if(searchFetchMission(uid, mid) == null) return Response.error("接包任务不存在");

        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("mid", mid);
        removeByMap(map);
        return Response.ok();
    }

    /**
     * 众包工人提交报告
     * @param fetchMissionVO 主报告内容
     * @return
     */
    @Override
    public Response submitReport(FetchMissionVO fetchMissionVO) {
        Mission mission = missionMapperService.selectById(fetchMissionVO.getMid());
        if(mission == null) return Response.error("测试任务不存在");
        if(mission.getState().equals("Done")) return Response.error("测试任务已结束");

        FetchMission fetchMission = searchFetchMission(fetchMissionVO.getUid(), fetchMissionVO.getMid());
        if(fetchMission == null) return Response.error("接包任务不存在");
        if((fetchMissionVO.getSubmitTime().getTime() - fetchMission.getFetchDate().getTime()) / (60 * 60 * 1000)
            > fetchMission.getTimeLimit().longValue()) return Response.error("提交已超过任务时限");

        fetchMission.setSubmitTime(fetchMissionVO.getSubmitTime());
        fetchMission.setTitle(fetchMissionVO.getTitle());
        fetchMission.setPicture1(fetchMissionVO.getPicture1());
        fetchMission.setCoordinate1(fetchMissionVO.getCoordinate1());
        fetchMission.setPicture2(fetchMissionVO.getPicture2());
        fetchMission.setCoordinate2(fetchMissionVO.getCoordinate2());
        fetchMission.setPicture3(fetchMissionVO.getPicture3());
        fetchMission.setCoordinate3(fetchMissionVO.getCoordinate3());
        fetchMission.setPicture4(fetchMissionVO.getPicture4());
        fetchMission.setCoordinate4(fetchMissionVO.getCoordinate4());

        fetchMission.setBugDescription(fetchMissionVO.getBugDescription());
        fetchMission.setBugRecurrentSteps(fetchMissionVO.getBugRecurrentSteps());
        fetchMission.setDeviceInformation(fetchMissionVO.getDeviceInformation());

        userService.increaseReputation(fetchMission.getUid());

        update(fetchMission, new UpdateWrapper<FetchMission>().and(i -> i.eq("uid", fetchMission.getUid()).eq("mid", fetchMission.getMid())));

        initializeSimilarity(fetchMission.getFid(), fetchMission.getMid());
        calReportSimilarity(fetchMission.getFid(), fetchMission.getMid());

//        计算图文匹配度
        fetchMission.setMatching(-1);
        fetchMission.setMatching(algorithmService.calMatching(fetchMission.getFid()));

        fetchMission.setTotal_score(CalTotalScore(fetchMission));
        fetchMission.setIsBad(JudgeIsBad(fetchMission));

        update(fetchMission, new UpdateWrapper<FetchMission>().and(i -> i.eq("uid", fetchMission.getUid()).eq("mid", fetchMission.getMid())));

        return Response.ok().put("fid", fetchMission.getFid());
    }


    /**
     * 查询测试任务下的所有报告
     * @param mid 测试任务mid
     * @param pageIndex 当前页数
     * @param pageSize 页大小
     * @return
     */
    @Override
    public MyPage<FetchMissionVO> searchReportsByMid(Long mid, Long pageIndex, Long pageSize, String mode) {
        List<String> params = checkMode(mode);
        if (params.isEmpty()){
            return new MyPage<>(new ArrayList<>(), 0, pageSize, pageIndex);
        }
        String order = params.get(1);
        QueryWrapper<FetchMission> queryWrapper = new QueryWrapper<FetchMission>().eq("mid", mid);
        if("asc".equals(order)){
            queryWrapper.orderByAsc(params.get(0));
        }else {
            queryWrapper.orderByDesc(params.get(0));
        }
        queryWrapper.isNotNull("submit_time");
        Page<FetchMission> page = new Page<>(pageIndex, pageSize);
        MyPage<FetchMission> fetchmissions_page = new MyPage<>(page(page, queryWrapper));
        return PageHelper.convert(fetchmissions_page, FetchMissionVO.class);
    }

    private ArrayList<String> checkMode(String mode){
        mode = mode.replaceAll("[A-Z]", "_$0").toLowerCase();
        StringBuilder key = new StringBuilder();
        String[] params = mode.split("_");
        for(int i = 0; i < params.length - 1; i++){
            if(i > 0) key.append("_");
            key.append(params[i]);
        }
        String order = params[params.length - 1];

        if(!("asc".equals(order) || "desc".equals(order))){
            return new ArrayList<>();
        }
        if(!("total_score".equals(key.toString()) || "submit_time".equals(key.toString()) || "score".equals(key.toString()))){
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(key.toString(), order));
    }


    /**
     * 根据uid和mid查询接包任务
     * @param uid 众包工人uid
     * @param mid 测试任务mid
     * @return
     */
    @Override
    public Response searchReportByIds(Long uid, Long mid) {
        FetchMission fetchMission = searchFetchMission(uid, mid);
        if (fetchMission == null) return Response.error("接包任务不存在");
        return Response.ok().put("fetchmission", new FetchMissionVO(searchFetchMission(uid, mid)));
    }

    /**
     * 根据fid查询接包任务
     * @param fid 接包任务fid
     * @return
     */
    @Override
    public Response searchReportByFid(Long fid){
        FetchMission fetchMission = getById(fid);
        if(fetchMission == null) return Response.error("接包任务不存在");
        return Response.ok().put("fetchmission", new FetchMissionVO(getById(fid)));
    }

    /**
     * 众包工人查询自己的接包任务
     * @param uid 众包工人uid
     * @param pageIndex 当前页数
     * @param pageSize 页大小
     * @return
     */
    @Override
    public MyPage<FetchMission> searchFetchMissionsByUid(Long uid, Long pageIndex, Long pageSize){
        Page<FetchMission> page = new Page<>(pageIndex, pageSize);
        return new MyPage<>(page(page, new QueryWrapper<FetchMission>().eq("uid", uid)));
    }

    /**
     * 查询测试任务下的所有低质量报告
     * @param mid 测试任务mid
     * @param pageIndex 当前页数
     * @param pageSize 页大小
     * @return
     */
    @Override
    public MyPage<FetchMissionVO> getBadReports(Long mid, Long pageIndex, Long pageSize){
        Page<FetchMission> page = new Page<>(pageIndex, pageSize);
        MyPage<FetchMission> myPage = new MyPage<>(page(page,new QueryWrapper<FetchMission>().eq("is_bad","True").eq("mid", mid)));
        return PageHelper.convert(myPage, FetchMissionVO.class);
    }

    /**
     * 众包工人提交报告后查询测试任务下与该报告最相似的三个报告
     * @param fid 接包任务fid
     * @param pageIndex 当前页数
     * @param pageSize 页大小
     * @return 三个最相似的报告
     */
    @Override
    public MyPage<FetchMissionVO> getSimilarReports(Long fid, Long pageIndex, Long pageSize) {
        List<ReportSimilarity> reportSimilarityList = reportSimilarityMapperService
                .selectList(new QueryWrapper<ReportSimilarity>().gt("similarity", 0.55).and(qw -> qw.eq("fid_A", fid).or().eq("fid_B", fid)));
        Collections.sort(reportSimilarityList, new Comparator<ReportSimilarity>() {
            @Override
            public int compare(ReportSimilarity o1, ReportSimilarity o2) {
                if (o1.getSimilarity() == o2.getSimilarity())
                    return 0;
                if (o1.getSimilarity() < o2.getSimilarity())
                    return 1;
                else
                    return -1;
            }
        });

        List<FetchMissionVO> records = new ArrayList<>();

        int count = 0;
        for(ReportSimilarity reportSimilarity : reportSimilarityList){
            Long thisFid = fid == reportSimilarity.getFidA() ?
                    reportSimilarity.getFidB() : reportSimilarity.getFidA();
            records.add(new FetchMissionVO(getById(thisFid)));
            count += 1;
            if (count == 3){
                break;
            }
        }
        return new MyPage<>(records, records.size(), pageSize, pageIndex);
    }

    /**
     * 返回该接包任务的报告与在其测试任务下的所有报告的相似度
     * @param fid 接包任务fid
     * @return
     */
    @Override
    public List<ReportSimilarityVO> getSimilarityGraph(Long fid) {
        List<ReportSimilarity> reportSimilarityList = reportSimilarityMapperService
                .selectList(new QueryWrapper<ReportSimilarity>().eq("fid_A", fid).or().eq("fid_B", fid));
        List<ReportSimilarityVO> rslt = new ArrayList<>();
        for(ReportSimilarity reportSimilarity : reportSimilarityList){
            ReportSimilarityVO reportSimilarityVO = new ReportSimilarityVO();
            reportSimilarityVO.setFid(fid == reportSimilarity.getFidA() ? reportSimilarity.getFidB() : reportSimilarity.getFidA());
            reportSimilarityVO.setSimilarity(reportSimilarity.getSimilarity());
            FetchMission fetchMission = getById(reportSimilarityVO.getFid());
            reportSimilarityVO.setTitle(fetchMission.getTitle());
            reportSimilarityVO.setMid(fetchMission.getMid());
            reportSimilarityVO.setUid(fetchMission.getUid());
            rslt.add(reportSimilarityVO);
        }
        return rslt;
    }

    /**
     * 查询测试任务下的已接包数
     * @param mid 测试任务mid
     * @return
     */
    @Override
    public Integer getMissionWorkersNum(Long mid){
        return count(new QueryWrapper<FetchMission>().eq("mid", mid));
    }

    /**
     * 通过mid和uid查询接包任务
     * @param mid 测试任务mid
     * @param uid 众包工人uid
     * @return
     */
    private FetchMission searchFetchMission(Long uid, Long mid){
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("mid", mid);
        return getOne(new QueryWrapper<FetchMission>().allEq(map));
    }

    /**
     * 初始化接包任务与统一测试任务下的接包任务的相似度
     * @param mid 接包任务所在的测试任务mid
     * @param thisFid 接包任务fid
     * @return
     */
    private void initializeSimilarity(Long thisFid, Long mid){
        List<ReportSimilarity> reportSimilarityList = reportSimilarityMapperService.selectList(new QueryWrapper<ReportSimilarity>().eq("fid_a", thisFid).or().eq("fid_b", thisFid));
        if (!reportSimilarityList.isEmpty())
            return;

        List<FetchMission> fetchMissions = list(new QueryWrapper<FetchMission>().eq("mid",mid));
        for (FetchMission fetchMission: fetchMissions){
            Long fid = fetchMission.getFid();
            if (fid.equals(thisFid))
                continue;
            ReportSimilarity reportSimilarity = new ReportSimilarity();
            reportSimilarity.setFidA(thisFid < fid ? thisFid : fid);
            reportSimilarity.setFidB(thisFid < fid ? fid : thisFid);
            reportSimilarityMapperService.insert(reportSimilarity);
        }
    }

    /**
     * 评论后重新计算接包任务的平均评分
     * @param thisScore 评论的评分
     * @param fid 接包任务fid
     * @return
     */
    public void calScore(Long fid, Integer thisScore){
        FetchMission fetchMission = getById(fid);

        if (fetchMission.getScore() < 0)
            fetchMission.setScore(0);

        double total = (fetchMission.getScore() * fetchMission.getCommentNum()) + thisScore;
        double thisavg = total / (fetchMission.getCommentNum() + 1);
        fetchMission.setCommentNum(fetchMission.getCommentNum() + 1);
        fetchMission.setScore(thisavg);

        fetchMission.setTotal_score(CalTotalScore(fetchMission));

//        调用判断功能
        fetchMission.setIsBad(JudgeIsBad(fetchMission));
        updateById(fetchMission);
    }

    /**
     * 评论后以及提交报告后重新计算综合评分
     * @param fetchMission 接包任务
     * @return
     */
    public double CalTotalScore(FetchMission fetchMission){
        double totalScore = 0;
        if (fetchMission.getScore() < 0){
            totalScore += Math.random() * 2.5;
        }
        else totalScore += fetchMission.getScore();

        if (fetchMission.getMatching() < 0 && !(fetchMission.getPicture1() == null)){
            totalScore += Math.random();
        }
        else if (fetchMission.getMatching() >= 0)
        {
            totalScore += fetchMission.getMatching();
        }

        return totalScore;
    }

    /**
     * 判断接包任务所提交的报告质量
     * @param fetchMission 接包任务
     * @return
     */
    public String JudgeIsBad(FetchMission fetchMission){
        if (fetchMission.getScore() < 0)
            return "Judging";
        //        TODO
//        这里对null可能还要处理一下
        if (fetchMission.getMatching() < 0 && !(fetchMission.getPicture1() == null))
            return "Judging";

        if (fetchMission.getScore() <= 2.0 || fetchMission.getMatching() < 0.5)
            return "True";
        else
            return "False";
    }

    private void calReportSimilarity(Long thisFid, Long mid){
        algorithmService.calReportSimilarity(thisFid, mid);
    }

    @Override
    public Response amplificationReport(FetchMissionVO fetchMissionVO) {
        Mission mission = missionMapperService.selectById(fetchMissionVO.getMid());
        if(mission == null) return Response.error("测试任务不存在");
        FetchMission fetchMission = searchFetchMission(fetchMissionVO.getUid(), fetchMissionVO.getMid());
        if(fetchMission == null) return Response.error("接包任务不存在");
        fetchMission.setSubmitTime(fetchMissionVO.getSubmitTime());
        fetchMission.setTitle(fetchMissionVO.getTitle());
        fetchMission.setPicture1(fetchMissionVO.getPicture1());
        fetchMission.setCoordinate1(fetchMissionVO.getCoordinate1());
        fetchMission.setPicture2(fetchMissionVO.getPicture2());
        fetchMission.setCoordinate2(fetchMissionVO.getCoordinate2());
        fetchMission.setPicture3(fetchMissionVO.getPicture3());
        fetchMission.setCoordinate3(fetchMissionVO.getCoordinate3());
        fetchMission.setPicture4(fetchMissionVO.getPicture4());
        fetchMission.setCoordinate4(fetchMissionVO.getCoordinate4());

        fetchMission.setBugDescription(fetchMissionVO.getBugDescription());
        fetchMission.setBugRecurrentSteps(fetchMissionVO.getBugRecurrentSteps());
        fetchMission.setDeviceInformation(fetchMissionVO.getDeviceInformation());

        userService.increaseReputation(fetchMission.getUid());

        update(fetchMission, new UpdateWrapper<FetchMission>().and(i -> i.eq("uid", fetchMission.getUid()).eq("mid", fetchMission.getMid())));
        update(fetchMission, new UpdateWrapper<FetchMission>().and(i -> i.eq("uid", fetchMission.getUid()).eq("mid", fetchMission.getMid())));

        return Response.ok().put("fid", fetchMission.getFid());
    }
}
