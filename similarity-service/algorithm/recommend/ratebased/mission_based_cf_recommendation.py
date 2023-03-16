import math
import os

import pandas as pd
from operator import itemgetter

#判断指标是否胜任的阈值,后续根据数据集进行修改。
standard_ability=0.1

#  基于物品的协同过滤推荐
class ItemCF():
    #  初始化参数
    def __init__(self):
        #要考虑的和当前任务相似的任务数，可以修改
        self.n_sim_missions = 3
        #  推荐的任务数
        self.n_rec_missions = 3
        self.train = {}
        #  任务元数据
        self.missions = 0
        self.m_cnt  = 0
        #  任务相似度度矩阵
        self.m_sim_matrix = {}
        print('系统将推荐 {0} 个任务'.format(self.n_rec_missions))

    #  载入数据
    def load_file(self, filepath):
        with open(filepath, 'r') as f:
            for i, line in enumerate(f):
                if i == 0:
                    continue
                yield line.strip('\r\n')

    #  读取数据集
    def read_dataset(self, filepath):
        #train_len代表整个数据集的行数。
        train_len = 0
        for line in self.load_file(filepath):
            uid, mid, rating = line.split(',')
            #只采用分数大于二的数据，即：胜任
            if float(rating)>=standard_ability:
                self.train.setdefault(uid, {})
                self.train[uid][mid] = rating
                train_len += 1
            else:
                pass

    #  清洗数据，获得任务元数据
    def mission_metadata(self, m_csv):
        self.missions = pd.read_csv(m_csv)
        self.m_cnt = len(self.missions)

    #  计算任务相似度
    def cal_m_sim(self):
        #  计算相似度矩阵
        m_popularity = {}
        for user, missions in self.train.items():
            for m1 in missions:
                #m_popularity记录评价每一类任务的总用户数。
                m_popularity.setdefault(m1, 0)
                m_popularity[m1] += 1
                for m2 in missions:
                    if m1 == m2:
                        continue
                    self.m_sim_matrix.setdefault(m1, {})
                    self.m_sim_matrix[m1].setdefault(m2, 0)
                    #m_sim_matric记录同时评价两类任务的用户数。
                    self.m_sim_matrix[m1][m2] += 1
        #少的列代表没有人同时评价过他们两个任务。
        for m1, m1_related in self.m_sim_matrix.items():
            for m2, count in m1_related.items():
                self.m_sim_matrix[m1][m2] = count / math.sqrt(m_popularity[m1] * m_popularity[m2])
        #注：最终不存在的列代表为两个任务之间的相似度为零，也就不予考虑。
    #  推荐任务
    def recommend(self, uid):
        watched = self.train[uid]
        rank = {}
        #寻找用户已好评的任务中相似的任务。
        for mid, rating in watched.items():
            #寻找和当前mid最相似的三个任务，相似度代表任务之间的相似度
            n_sim_missions = sorted(self.m_sim_matrix[mid].items(), key=itemgetter(1), reverse=True)[:self.n_sim_missions]
            for m, similarity in n_sim_missions:
                rank.setdefault(m, 0)
                rank[m] += similarity * float(rating)
        #rank记录的是所有可能的任务的推荐指数。
        #排好序取三个

        #指标归一化
        #默认原指标的数据上限是二的10次幂,将指标归一化为0-100
        for each in rank:
            rank[each]= math.log(rank[each]+1)*10
        rank = sorted(rank.items(), key=itemgetter(1), reverse=True)[:self.n_sim_missions]
        #元组列表转字典,且把所有的键由字符串转为数字
        output={}
        for each in rank:
            output[int(each[0])]=each[1]
        return output


def excute(uid):
    uid=str(uid)
    #这两句用于处理main中os调用的错误。
    current_path = os.path.dirname(__file__)
    os.chdir(current_path)
    m_csv = './data/missions.csv'
    with open(m_csv, "r", newline='') as file:
        pass
    r_csv = './data/ratings.csv'
    #创建类，赋初始值
    engine = ItemCF()
    #处理两个输入
    engine.read_dataset(r_csv)
    engine.mission_metadata(m_csv)
    #计算任务之间的相似度
    engine.cal_m_sim()
    print('对于用户 {0}的推荐结果为：'.format(uid))
    return engine.recommend(uid)

# result=excute(5)
# print(result)