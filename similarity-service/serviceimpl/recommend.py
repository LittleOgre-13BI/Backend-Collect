from .prepare_data import *
from algorithm.recommend.docbased.code.executor import excute as doc_exec
from algorithm.recommend.ratebased.mission_based_cf_recommendation import excute as mission_exec
from algorithm.recommend.ratebased.user_based_cf_recommendation import excute as user_exec


def filter(recommend_dict, uid):
    rslt = dict()
    t_mids = user_todo_mids(uid)
    count = 0
    print(recommend_dict)
    print(t_mids)
    for key in recommend_dict.keys():
        if key in t_mids:
            count += 1
            rslt[key] = recommend_dict[key]
            if count == 3:
                break
    return rslt



class RecommendStrategy(object):
    def build_csv(self, uid):
        pass

    def recommend(self, uid):
        pass


class DocBasedRecommend(RecommendStrategy):
    def build_csv(self, uid):
        build_doneDocsDescription_csv(uid)
        build_allDocsDescription_csv(uid)

    def recommend(self, uid):
        print("doc based recommend!")
        return filter(doc_exec(), uid)
        # return doc_exec()


class UserRateBasedRecommend(RecommendStrategy):
    def build_csv(self, uid):
        build_rating_csv()
        build_missions_csv(uid)

    def recommend(self, uid):
        print("user based recommend!")
        return filter(user_exec(uid), uid)
        # return user_exec(uid)


class MissionRateBasedRecommend(RecommendStrategy):
    def build_csv(self, uid):
        build_rating_csv()
        build_missions_csv(uid)

    def recommend(self, uid):
        print("mission based recommend!")
        return filter(mission_exec(uid), uid)
        # return mission_exec(uid)


class RecommendInterface(object):
    def __init__(self, recommend_strategy):
        self.recommend_strategy = recommend_strategy

    def set_strategy(self, recommend_strategy):
        self.recommend_strategy = recommend_strategy

    def recommend(self, uid):
        self.recommend_strategy.build_csv(uid)
        return self.recommend_strategy.recommend(uid)

    def get_stratepy(self):
        return type(self.recommend_strategy).__name__


recommend_service = RecommendInterface(DocBasedRecommend())


