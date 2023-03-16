from flask import Blueprint, request


from algorithm.augment.code.eda import reportAugment
from serviceimpl import similarity_one
from serviceimpl.recommend import *
from serviceimpl import matching_handler
import paddlehub as hub

algrithm = Blueprint("algorithm", __name__)
lda_new = hub.Module(name="lda_news")


@algrithm.route('/recommend', methods=['post'])
def recommand_mission():
    uid = request.values.get('uid')
    return recommend_service.recommend(uid)


@algrithm.route('/similarity', methods=['post'])
def similarity_counter():
    fid = request.values.get('fid')
    mid = request.values.get('mid')
    return similarity_one.similarity_one(fid, mid)
    # return "ok"


@algrithm.route('/matching', methods=['post'])
def cal_matching():
    fid = request.values.get('fid')
    result = matching_handler.calMatching(fid, lda_new)
    if result > 5:
        result = 5
    return str(result)
    # print(fid)
    # return "1.0"

@algrithm.route('/setstrategy', methods=['post'])
def set_strategy():
    strategy = request.json['recommend']
    if strategy == 'mission_based':
        recommend_service.set_strategy(MissionRateBasedRecommend())
    elif strategy == 'user_based':
        recommend_service.set_strategy(UserRateBasedRecommend())
    elif strategy == 'doc_based':
        recommend_service.set_strategy(DocBasedRecommend())
    return strategy


@algrithm.route('/getstrategy', methods=['get'])
def get_strategy():
    rslt = dict()
    recommend_strategy = recommend_service.get_stratepy()
    if recommend_strategy == 'DocBasedRecommend':
        recommend_strategy = 'doc_based'
    elif recommend_strategy == 'MissionRateBasedRecommend':
        recommend_strategy = 'mission_based'
    elif recommend_strategy == 'UserRateBasedRecommend':
        recommend_strategy = 'user_based'

    rslt['recommend'] = recommend_strategy
    rslt['similarity'] = 'basic_similarity'
    return rslt

@algrithm.route('/amplification', methods=['post'])
def amplification():
    amplificationText = reportAugment(request.json['title'],request.json['bugDescription'],request.json['bugRecurrentSteps'],request.json['deviceInformation'])
    rslt = dict()
    keyName = ['title','bugDescription','bugRecurrentSteps','deviceInformation']
    # amplificationText = ["旋转图片",
    #                     "提示点击图片可以旋转图片，但没有反应",
    #                     "进入优秀反馈展示 点击图片",
    #                     "用了一段时间的安卓机"]
    for i in range(0,4):
        rslt[keyName[i]] = amplificationText[i]
    return rslt





