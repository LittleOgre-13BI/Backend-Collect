package com.controller;

import com.service.FetchMissionService;
import com.service.MissionService;
import com.vo.MissionVO;
import com.vo.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.utils.Response;

import java.util.List;

@CrossOrigin(origins = "http://124.221.127.36", maxAge = 3600, allowCredentials="true",allowedHeaders = "*",methods = {RequestMethod.POST,RequestMethod.GET})
@RestController
@RequestMapping("/mission")
public class MissionController {
    @Autowired
    private MissionService missionService;

    @Autowired
    private FetchMissionService fetchMissionService;


    /**
     * 发布任务
     * ..
     */
    @PostMapping("/addmission")
    public Response addMission(@RequestBody List<MissionVO> missionVOs){
        return missionService.addMission(missionVOs);
    }

    /**
     * 发包方删除任务
     */
    @PostMapping("/removemission")
    public Response removeMission(Long mid){
        return missionService.removeMission(mid);
    }

//	/**
//     * 管理员和发包方更新任务
//     */
//    @PostMapping(value ="/updatemission")
//    public Response updateMission(Long mid, @RequestBody MissionVO missionVO){
//        missionVO.setMid(mid);
//        return missionService.updateMission(missionVO);
//    }


	 /**
     * 我的任务（众包工人查询与自己有关的任务列表）
     */
     @GetMapping("/workersearchmissionsbyuid")
     public MyPage<MissionVO> workerSearchMissionsByUid(Long uid, Long pageIndex, Long pageSize){
         return missionService.workerSearchMissionsByUid(fetchMissionService.searchFetchMissionsByUid(uid, pageIndex, pageSize));
     }

    /**
     * 我的任务（发包方查询与自己有关的任务列表）
     * 发包方查询的表和众包工人查询的表不同，发包方：mission；众包工人：fetch_mission
     */
    @GetMapping("/sendersearchmissionsbyuid")
    public MyPage<MissionVO> senderSearchMissionsByUid(Long uid, Long pageIndex, Long pageSize){
        return missionService.senderSearchMissionsByUid(uid, pageIndex, pageSize);
    }

    /**
     * 查询全部任务
     */
    @GetMapping("/searchallmissions")
    public MyPage<MissionVO> searchAllMissions(){
        return missionService.searchAllMissions();
    }

    /**
     * 根据主键查找某个任务
     */
    @GetMapping("/searchmissionbymid")
    public Response searchMissionByMid(Long mid){
        return missionService.searchMissionByMid(mid);
    }

    /**
     * 根据任务名查找任务
     */
    @GetMapping("/searchmissionsbymissionname")
    public MyPage<MissionVO> searchMissionsByMissionName(String missionName, Long pageIndex, Long pageSize){
        return missionService.searchMissionsByMissionName(missionName, pageIndex, pageSize);
    }

    /**
     * 为众包工人推荐任务
     */
    @GetMapping("/recommendation")
    public MyPage<MissionVO> missionRecommendation(Long uid, Long pageIndex, Long pageSize){
        return missionService.missionRecommendation(uid, pageIndex, pageSize);
    }

//    @GetMapping("/test")
//    public List<Long> test(){
//        return algorithmService.recommandMissions(2L);
//    }

}
