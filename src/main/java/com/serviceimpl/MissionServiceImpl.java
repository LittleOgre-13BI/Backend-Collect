package com.serviceimpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mapperservice.FetchMissionMapperService;
import com.mapperservice.MissionMapperService;
import com.mapperservice.UserMapperService;
import com.po.FetchMission;
import com.po.Mission;
import com.po.User;
import com.service.AlgorithmService;
import com.service.FetchMissionService;
import com.service.MissionService;
import com.service.UserService;
import com.utils.PageHelper;
import com.utils.Response;
import com.vo.MissionVO;
import com.vo.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("missionService")
public class MissionServiceImpl extends ServiceImpl<MissionMapperService, Mission> implements MissionService {

    private final FetchMissionMapperService fetchMissionMapperService;

    private final UserMapperService userMapperService;

    private final AlgorithmService algorithmService;

    @Autowired
    public MissionServiceImpl(FetchMissionMapperService fetchMissionMapperService, UserMapperService userMapperService, AlgorithmService algorithmService){
        this.fetchMissionMapperService = fetchMissionMapperService;
        this.userMapperService = userMapperService;
        this.algorithmService = algorithmService;
    }

    /**
     * 发包方发布测试任务
     * @param missionVOs 测试任务信息
     * @return
     */
    @Override
    public Response addMission(List<MissionVO> missionVOs) {
        for (MissionVO missionVO : missionVOs) {
            User user = userMapperService.selectById(missionVO.getUid());
            if(user == null){
                return Response.error("用户不存在");
            }
            if(user.getUserType().equals("employee")){
                return Response.error("众包工人不能发布测试任务");
            }
            missionVO.setLastFetchTime(missionVO.getRecruitStart());
            missionVO.setState("Recruiting");
            save(new Mission(missionVO));
        }
        return Response.ok();
    }

    /**
     * 删除测试任务
     * @param mid 测试任务mid
     * @return
     */
    @Override
    public Response removeMission(Long mid) {
        if(selectMissionByMid(mid) == null){
            return Response.error("测试任务不存在");
        }
        Date curr = new Date();
        Mission mission = selectMissionByMid(mid);

        if(mission.getLastFetchTime().getTime() + Long.valueOf(mission.getTimeLimit()) * 3600000 > curr.getTime()){
            return Response.error("测试任务正在进行中，不可删除，请在任务结束后再删除任务");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mid", mid);
        fetchMissionMapperService.deleteByMap(map);
        removeById(mid);
        return Response.ok();
    }

    /**
     * 众包工人查询自己所有接过的测试任务
     * @param fetchMissionList 众包工人的接包任务列表
     * @return
     */
    @Override
    public MyPage<MissionVO> workerSearchMissionsByUid(MyPage<FetchMission> fetchMissionList) {
        List<MissionVO> records = new ArrayList<>();
        for(FetchMission f : fetchMissionList.getList()){
            Long mid = f.getMid();
            if(isMissionExist(mid))
                records.add(new MissionVO(selectMissionByMid(f.getMid())));
        }
        return new MyPage<>(records, fetchMissionList.getTotal(), fetchMissionList.getPageSize(), fetchMissionList.getCurrPage());
    }

    /**
     * 发包方查询自己发布的测试任务
     * @param uid 发包方uid
     * @param pageIndex 当前页数
     * @param pageSize 页大小
     * @return
     */
    @Override
    public MyPage<MissionVO> senderSearchMissionsByUid(Long uid, Long pageIndex, Long pageSize) {
        Page<Mission> page = new Page<>(pageIndex, pageSize);
        MyPage<Mission> missions_page = new MyPage<>(page(page, new QueryWrapper<Mission>().eq("uid", uid)));
        return PageHelper.convert(missions_page, MissionVO.class);
    }

    /**
     * 查询所有的测试任务
     * @return
     */
    @Override
    public MyPage<MissionVO> searchAllMissions() {
        return searchMissionsByMissionName("", 1L, 10L);
    }

    /**
     * 根据mid查询测试任务
     * @param mid 测试任务mid
     * @return
     */
    @Override
    public Response searchMissionByMid(Long mid) {
        if(isMissionExist(mid)) return Response.ok().put("mission", new MissionVO(getById(mid)));
        return Response.error("测试任务不存在");
    }

    /**
     * 根据测试任务名称模糊查询
     * @param missionName 测试任务名称
     * @param pageIndex 当前页数
     * @param pageSize 页大小
     * @return
     */
    @Override
    public MyPage<MissionVO> searchMissionsByMissionName(String missionName, Long pageIndex, Long pageSize) {
        Page<Mission> page = new Page<>(pageIndex, pageSize);
        MyPage<Mission> missions_page = new MyPage<>(page(page, new QueryWrapper<Mission>().like("name", missionName)));
        return PageHelper.convert(missions_page, MissionVO.class);
    }

    /**
     * 获取测试任务限时
     * @param mid 测试任务mid
     * @return
     */
    @Override
    public Integer getMissionTimeLimit(Long mid) {
        if(isMissionExist(mid)) return selectMissionByMid(mid).getTimeLimit();
        return 0;
    }

    /**
     * 查询测试任务是否存在
     * @param mid 测试任务mid
     * @return
     */
    @Override
    public boolean isMissionExist(Long mid){
        return selectMissionByMid(mid) != null;
    }

    /**
     * trigger更新任务状态，频率为每三十秒一次
     */
    @Override
    public void updateMissionState() {
        List<Mission> missionList = list();
        assert !missionList.isEmpty();
        for(Mission m : missionList){
            if(m.getState().equals("Done")) continue;
            Date currTime = new Date();
            if(m.getState().equals("Ongoing")){
                if(currTime.getTime() > (m.getLastFetchTime().getTime() + m.getTimeLimit().longValue() * 3600000)){
                    m.setState("Done");
                    System.out.println("Mission: " + m.getMid() + "transform to Done");
                }
            }
            else if(m.getState().equals("Recruiting")){
                if(fetchMissionMapperService.selectCount(new QueryWrapper<FetchMission>().eq("mid", m.getMid())).equals(m.getWorkerNum())){
                    m.setState("Ongoing");
                    System.out.println("Mission: " + m.getMid() + "transform to Ongoing");
                }
                else if(currTime.after(m.getRecruitEnd())){
                    m.setState("Ongoing");
                    System.out.println("Mission: " + m.getMid() + "transform to Ongoing");
                }
            }
            saveMission(new MissionVO(m));
        }
    }

    /**
     * 为众包工人推荐任务
     * @param uid 众包工人uid
     * @param pageIndex 当前页数
     * @param pageSize 页大小
     * @return
     */
    @Override
    public MyPage<MissionVO> missionRecommendation(Long uid, Long pageIndex, Long pageSize) {
        List<Long> recommend_mids = algorithmService.recommendMissions(uid);
        List<MissionVO> records = new ArrayList<>();
        for(Long mid : recommend_mids){
            records.add(new MissionVO(getById(mid)));
        }
        return new MyPage<>(records, records.size(), pageSize, pageIndex);
    }

    /**
     * 根据mid获取测试任务
     * @param mid
     * @return
     */
    private Mission selectMissionByMid(Long mid){
        return getById(mid);
    }

    /**
     * 发包方或管理员修改测试任务
     * @param missionVO 测试任务详细信息
     * @return
     */
    public Response saveMission(MissionVO missionVO){
        if(!isMissionExist(missionVO.getMid())) return Response.error("测试任务不存在");
        updateById(new Mission(missionVO));
        return Response.ok();
    }
}
