import math

# import algorithm.recommend.docbased.code.executor
# from algorithm.recommend.docbased.code.executor import excute as doc_exec
# from algorithm.recommend.ratebased.mission_based_cf_recommendation import excute as mission_exec
# from algorithm.recommend.ratebased.user_based_cf_recommendation import excute as user_exec
import os
from db.mission import *
from db.fetchmission import *
from db.user import *
import csv


def user_todo_mids(uid):
    t_mids = []
    user_devices = search_user_devices(uid)
    tuples = search_all_recruiting_mission_mid_and_device()
    already_fetched = search_fetchmission_mids_by_uid(uid)
    for tuple in tuples:
        if tuple[1] not in user_devices:
            continue
        if tuple[0] in already_fetched:
            continue
        t_mids.append(tuple[0])
    return t_mids


def build_missions_csv(uid):
    #这两句用于处理main中os调用的错误。
    current_path = os.path.dirname(__file__)
    os.chdir(current_path)
    print(os.getcwd())

    tuples = search_all_recruiting_mission_mid_and_device()
    file_name = '../algorithm/recommend/ratebased/data/missions.csv'
    with open(file_name, 'w', newline='', encoding='utf-8') as file:
        writer = csv.writer(file)
        writer.writerow(["mid", "type"])
        for tuple in tuples:
            labels = "|".join(search_mission_labels(tuple[0]))
            writer.writerow([tuple[0], labels])


def cal_ability(uid, mid):
    ability = 0.0
    mission_labels = search_mission_labels(mid)
    fetcheds = search_fetchmission_mids_by_uid(uid)
    for mission_label in mission_labels:
        this_score = 0
        this_num = 0
        for fetched in fetcheds:
            fetched_labels = search_mission_labels(fetched)
            if mission_label in fetched_labels:
                this_score += search_score(uid, fetched)
                this_num += 1
            else:
                continue

        if this_num > 0:
            ability += (this_score * 1.0) / this_num

    if len(mission_labels) > 0:
        ability = ability / len(mission_labels)
    # print(uid, mid, ability)
    return ability


def cal_interest(uid, mid):
    user_labels = search_user_labels(uid)
    mission_labels = search_mission_labels(mid)
    inter = len(set(user_labels).intersection(set(mission_labels))) * 1.0
    union = len(set(user_labels).union(set(mission_labels))) * 1.0
    fakeJaccard = inter / union
    # print(uid, mid, fakeJaccard)
    return fakeJaccard


def rating(uid, mid):
    difficulty = search_difficulty(mid) / 5.0
    reputation = math.log(search_reputation(uid) + 1) / 6.0
    interest = cal_interest(uid, mid)
    ability = cal_ability(uid, mid) / 5.0

    mission_layer = (difficulty + interest) / 2.0
    user_layer = (interest + reputation + ability) / 3.0

    return (mission_layer + user_layer) / 2.0


def build_rating_csv():
    #这两句用于处理main中os调用的错误。
    current_path = os.path.dirname(__file__)
    os.chdir(current_path)
    print(os.getcwd())

    units = []
    uids = search_all_uids()
    tuples = search_all_mission_mid_and_device()
    for uid in uids:
        for tuple in tuples:
            units.append([uid, tuple[0], rating(uid, tuple[0])])
    file_name = "../algorithm/recommend/ratebased/data/ratings.csv"
    with open(file_name, 'w', newline='', encoding='utf-8') as file:
        writer = csv.writer(file)
        writer.writerow(["uid","mid","rating"])
        writer.writerows(units)


def build_allDocsDescription_csv(uid):
    #这两句用于处理main中os调用的错误。
    current_path = os.path.dirname(__file__)
    os.chdir(current_path)
    print(os.getcwd())

    all_mid_device = search_all_mission_mid_and_device()
    file_name = '../algorithm/recommend/docbased/data/allDocsDescription.csv'
    with open(file_name,'w',newline='', encoding='utf-8') as file:
        writer = csv.writer(file)
        writer.writerow(["mid","url"])
        for mid_device in all_mid_device:
            tuple = search_mission_mid_and_url(mid_device[0])
            writer.writerow(tuple)


def build_doneDocsDescription_csv(uid):
    #这两句用于处理main中os调用的错误。
    current_path = os.path.dirname(__file__)
    os.chdir(current_path)
    print(os.getcwd())

    already_fetched = search_fetchmission_mids_by_uid(uid)
    file_name = '../algorithm/recommend/docbased/data/doneDocsDescription.csv'
    with open(file_name, 'w', newline='', encoding='utf-8') as file:
        writer = csv.writer(file)
        writer.writerow(['mid', 'url'])
        for mid in already_fetched:
            tuple = search_mission_mid_and_url(mid)
            writer.writerow(tuple)


# def recommend_one(uid):
    # build_missions_csv(uid)
    # build_rating_csv()
    # build_allDocsDescription_csv(uid)
    # build_doneDocsDescription_csv(uid)
    # rslt = dict()
    # recommend_rslt = user_exec(uid)
    # t_mids = user_todo_mids(uid)
    # for key in recommend_rslt.keys():
    #     if key in t_mids:
    #         rslt[key] = recommend_rslt[key]
    # return rslt

# print(recommend_one(2))

# build_missions_csv(2)