package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.po.FetchMission;
import com.po.Mission;


import com.utils.Response;
import com.vo.MissionVO;
import com.vo.MyPage;

import java.util.List;


public interface MissionService extends IService<Mission> {

	Response addMission(List<MissionVO> missionVOs);

    Response removeMission(Long mid);

//    Response updateMission(MissionVO missionVO);

    MyPage<MissionVO> workerSearchMissionsByUid(MyPage<FetchMission> fetchMissionList);

    MyPage<MissionVO> senderSearchMissionsByUid(Long uid, Long pageIndex, Long pageSize);

    MyPage<MissionVO> searchAllMissions();

    Response searchMissionByMid(Long mid);

    MyPage<MissionVO> searchMissionsByMissionName(String missionName, Long pageIndex, Long pageSize);

    Integer getMissionTimeLimit(Long mid);

    boolean isMissionExist(Long mid);

    Response saveMission(MissionVO missionVO);

    void updateMissionState();

    MyPage<MissionVO> missionRecommendation(Long uid, Long pageIndex, Long pageSize);
}

