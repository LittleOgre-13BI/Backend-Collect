import math
import os

import pandas as pd
from operator import itemgetter


#判断指标是否胜任的阈值,后续根据数据集进行修改。
standard_ability=0.1

#  基于用户的协同过滤推荐
class UserCF():
    #  初始化参数
    def __init__(self):
        #  推荐3项任务
        self.n_sim_users  = 3
        self.n_rec_missions = 3

        self.train = {}

        #  任务元数据
        self.missions = 0
        self.m_cnt  = 0

        #  用户相似度矩阵
        self.user_sim_matrix = {}

        print('系统将推荐 {0} 项任务'.format(self.n_rec_missions))

    #  载入数据
    def load_file(self, filepath):
        with open(filepath, 'r') as f:
            for i, line in enumerate(f):
                if i == 0:
                    continue
                yield line.strip('\r\n')

    #  读取数据集
    def read_dataset(self, filepath):
        for line in self.load_file(filepath):
            #只处理能够胜任的评分
            uid, m_id, rating = line.split(',')
            if float(rating)>=standard_ability:
                self.train.setdefault(uid, {})
                self.train[uid][m_id] = rating
            else:
                pass

    #  清洗数据，获得电影元数据
    def mission_metadata(self, m_csv):
        self.missions = pd.read_csv(m_csv)
        self.m_cnt  = len(self.missions)

    #  计算用户相似度
    def cal_user_sim(self):
        #  建立电影-用户倒排表
        mission_user = {}
        for user, missions in self.train.items():
            for mission in missions:
                mission_user.setdefault(mission, set())
                mission_user[mission].add(user)
        #  计算相似度矩阵
        for mission, users in mission_user.items():
            for u in users:
                for v in users:
                    self.user_sim_matrix.setdefault(u, {})
                    self.user_sim_matrix[u].setdefault(v, 0)
                    self.user_sim_matrix[u][v] += 1
        for u, related_users in self.user_sim_matrix.items():
            for v, count in related_users.items():
                n_u = len(self.train[u])
                n_v = len(self.train[v])
                self.user_sim_matrix[u][v] = count / math.sqrt(n_u * n_v)

    #  推荐任务
    def recommend(self, uid):
        rank = {}
        watched = self.train[uid]
        n_sim_users = sorted(self.user_sim_matrix[uid].items(), key=itemgetter(1), reverse=True)[:self.n_sim_users]
        for v, w_uv in n_sim_users:
            for mission, rating in self.train[v].items():
                rank.setdefault(mission, 0)
                rank[mission] += w_uv * float(rating)
        #指标归一化
        #默认原指标的范围一般在0和16之间，归一化到100 注：加一是为了防止负数
        for each in rank:
            rank[each]= math.log(rank[each]+1)*25
        rank = sorted(rank.items(), key=itemgetter(1), reverse=True)
        #元组列表转字典,且把所有的键由字符串转为数字
        output={}
        for each in rank:
            output[int(each[0])]=each[1]
        return output
        return rank


def excute(uid):
    uid = str(uid)
    #这两句用于处理main中os调用的错误。
    current_path = os.path.dirname(__file__)
    os.chdir(current_path)
    m_csv = './data/missions.csv'
    r_csv = './data/ratings.csv'
    # uid = '3'
    engine = UserCF()
    engine.read_dataset(r_csv)
    engine.mission_metadata(m_csv)
    engine.cal_user_sim()
    print('对于用户 {0} 的推荐结果为：'.format(uid))
    rec_missions = engine.recommend(uid)
    return rec_missions
# result=excute()
# print(result)