# -*- coding:UTF-8 -*-
import os

from gensim.models import word2vec
import numpy as np
import gensim
import jieba
import io
import jieba.analyse
import pickle

from numpy import mean
from scipy import spatial
import sys

#存储已完成任务描述集 对 待选择报告的平均相似度。（每一个元素为元组，前一个值为文件名，后一个值为平均相似度）
result=[]

#规定字符格式
default_encoding = 'utf-8'
if sys.getdefaultencoding() != default_encoding:
    sys.reload()
    sys.setdefaultencoding(default_encoding)


#读取文件
def read_sentence(filename):
    """读取一个文件并转换为一行,并去符号"""
    sentenceline = []
    with io.open(filename, 'r', encoding='utf-8') as f:
        sentenceline =  f.read().replace('\n', '').replace('\t', '').replace('\u3000', '').replace('<','').replace('>','').replace('(','')\
                    .replace(')','').replace('?','').replace('，','').replace('　　　　　','')\
                    .replace('：','').replace('　','').replace('“','').replace('”','').replace('.','').replace('、','').replace('》','').replace('《','')\
                    .replace('（','').replace('）','').replace(']','').replace('[','').replace('］','').replace('［','')
        return sentenceline

# txt转为list
def eachLine(fileName):
    with io.open(fileName, "r", encoding='utf-8') as f:
        lines = f.readlines()
        return (lines)#获得lines

#求余弦距离
def M_cosine(v1,v2):

    v1=np.array(v1)
    v2=np.array(v2)

    sim=1-spatial.distance.cosine(v1,v2)
    return sim

# 计算平均相似度
def count_similar(sentencetxt,dictlines,TopK = 3):
#句子处理  暂定sentence是txt文件，打开之后做读为一行，去符号之后，就进入tf和w2v获得向量一个
    sentenceline = read_sentence(sentencetxt)
    # 按行处理 取一个文本的前100关键词
    tups = jieba.analyse.extract_tags(sentenceline, topK=100, withWeight=True, allowPOS=())
    numtups = len(tups)  # 实际取的关键词数
    i = 0
    word_xweight = 0
    model = gensim.models.Word2Vec.load('../data/tmp/w2vmodel')
    while i < numtups:  # 遍历每一个关键词 i是关键词数
        word = tups[i][0]
        weight = tups[i][1]
        try:
            tryw2v = model.wv[word]
        except KeyError:
            tryw2v = 0
        word_xweight = word_xweight + (weight * tryw2v)
        i = i + 1
        try:
            sentence_w2v = word_xweight / numtups
        except ZeroDivisionError:
            print ("该文件没有内容")


    dict_sims = {}#存储{sim:i}
    sim_list = []#存储sim从大到小

    for i in range(len(dictlines)):
        # dict_sim {相似度：文本序号}
        dict_sims[i] = M_cosine(sentence_w2v,dictlines[i])
    # 相似度排序[] 获得从大到小
    sim_list=sorted(dict.items(dict_sims),key= lambda d:d[1],reverse=True)

    #求平均值
    sim_value_list=[]
    for eachTuple in sim_list:
        sim_value_list.append(eachTuple[1])
    return mean(sim_value_list)



    # 此处是原相似度排序，在本任务中只需求平均值。
    # for i in range(TopK):
    #     # 获得名称
    #     dictnames = pickle.load(open('../data/tmp/names.txt','rb'))
    #     thisname = dictnames[sim_list[i][0]]
    #     print ("相似文本名称："+thisname+"  "+"相似度："+str(sim_list[i][1]))

def tfexcute():
    # 要测试的一个文件txt
    result=dict()
    cat_dir = os.path.join('../data/allDocsDescription')
    files = os.listdir(cat_dir)
    for eachfile in files:
        sentencetxt= '../data/allDocsDescription/'+eachfile
        dictlines = pickle.load(open('../data/tmp/dictlines','rb'))
        similar=count_similar(sentencetxt,dictlines)
        result[eachfile]=similar
    result = sorted(result.items(), key=lambda x: x[1], reverse=True)
    #result里面记录了对于每一个任务的匹配程度。
    # print(result)

    #取前三项：
    # topK=3
    # result=result[0:topK]

    #将结果转为字典，另外原键名为mid.txt，将其转化为mid,结果存在output中
    output={}
    for i in result:
        output[int(i[0][:-4])]=i[1]
    return output