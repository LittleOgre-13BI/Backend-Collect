import json
import math

# import paddlehub as hub
import Lda_single
#该变量由cal_text_sim方法调用，为了只加载一次模型，故写在前面。
# global lda_news
# lda_news = None


class Position:
    column_min = 0
    column_max = 0
    row_min = 0
    row_max = 0

    def __init__(self, c_min, c_max, r_min, r_max):
        self.column_min = c_min
        self.column_max = c_max
        self.row_min = r_min
        self.row_max = r_max


def calWeight(componentPosition, targetPosition):
    #求bug区域面积
    componentLength = componentPosition.row_max-componentPosition.row_min
    componentWidth = componentPosition.column_max-componentPosition.column_min
    componentArea = componentLength*componentWidth
    targetLength = targetPosition.row_max-targetPosition.row_min
    targetWidth = targetPosition.column_max-targetPosition.column_min
    targetArea = targetLength * targetWidth
    #求重叠部分面积
    #方法：
    #大值取小，小值取大
    #右边minX-左边maxX就是宽,然后乘以上边minY - 下边maxY
    min_row_max = min(componentPosition.row_max, targetPosition.row_max)
    max_row_min = max(componentPosition.row_min, targetPosition.row_min)
    min_column_max = min(componentPosition.column_max, targetPosition.column_max)
    max_column_min = max(componentPosition.column_min, targetPosition.column_min)
    rowDistance = max_row_min-min_row_max
    columnDistance = max_column_min-min_column_max
    #不相交的部分赋值环境权重0.01
    if(rowDistance >= 0 or columnDistance >= 0):
        return 0.01
    else:
        thisArea = rowDistance * columnDistance
        #weight为交并比
        weight = thisArea / (componentArea + targetArea - thisArea)
        if weight <= 0.01:
            return 0.01
        else:
            return weight


def cal_text_sim(shortText, longText, lda_news):
    #使用百度paddlehub计算相似度。
    #计算的是短文本和长文本之间的语义相似度。
    #使用的lda_news的初始化在类的开头。
    # global lda_news
    # if lda_news == None:
    #     lda_news = hub.Module(name="lda_news")
    lda_sim = lda_news.cal_query_doc_similarity(query=shortText,
                                                document=longText)
    return lda_sim


def cal_total_sim(descriptrion, jsonurl, targetPosition, lda_news):
    #文本组件的相似度
    text_sim = 0
    #非文本组件的相似度
    non_text_sim = 0
    f = open(jsonurl, 'r', encoding='utf-8')
    jsonFile = json.load(f)
    for eachComponent in jsonFile['compos']:
        #首先获得组件的位置信息，计算出组件的权重
        column_min = eachComponent['position']['column_min']
        column_max = eachComponent['position']['column_max']
        row_min = eachComponent['position']['row_min']
        row_max = eachComponent['position']['row_max']
        componentPosition = Position(column_min, column_max, row_min, row_max)
        weight = calWeight(componentPosition, targetPosition)
        # 先处理非文本组件
        if eachComponent["class"] != "Text":
            componentTable = {'Button': '按钮', 'CheckBox': '复选框', 'Chronometer': '计时器',
                            'EditText': '输入框', 'ImageButton': '图片按钮', 'ImageView': '图像视图',
                            'ProgressBar': '进度条', 'RadioButton': '单选按钮', 'RatingBar': '星级评分条',
                            'SeekBar': '拖动条', 'Spinner': '下拉控件', 'Switch': '开关', 'ToggleButton': '开关按钮',
                            'VideoView': '视频播放器', 'TextView': '文本框'
                            }
            componentName = componentTable[eachComponent["class"]]
            similarity = cal_text_sim(componentName, descriptrion, lda_news)
            non_text_sim = non_text_sim+similarity*weight
            continue
        else:
            componentText = eachComponent["text_content"]
            similarity = cal_text_sim(componentText, descriptrion, lda_news)
            text_sim = text_sim+similarity*weight
            continue
    #文字组件相似度和非文字组件相似度共同考虑
    total_sim = 0.8 * text_sim + 0.2 * non_text_sim
    #为了后续处理，结果乘以500
    total_sim = total_sim*500.0
    return total_sim
# a=Position(0,800,0,450)
# print(cal_total_sim("点击抽奖按钮三秒 点击停止抽奖 预期结果：转盘成功停止转动，抽奖成功 实际结果： 抽奖失败",jsonurl="./data/output/merge/4.json",targetPosition=a))
