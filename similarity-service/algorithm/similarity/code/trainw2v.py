# -*- coding:UTF-8 -*-
from gensim.models import word2vec
import sys
default_encoding = 'utf-8'
if sys.getdefaultencoding() != default_encoding:
    sys.reload()
    sys.setdefaultencoding(default_encoding)


def get_model(file_fenci):
    # w2v模型的建立和保存
    sentences =word2vec.Text8Corpus(file_fenci)
    model = word2vec.Word2Vec(sentences,size=100,min_count=1)
    model.save('../data/tmp/w2vmodel')


if __name__ == '__main__':
    #用来训练w2v，需要输入已经处理好，分词的文件
    file_fenci = '../data/tmp2/split.txt'
    dictlines = get_model(file_fenci)