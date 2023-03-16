# -*- coding:utf-8 -*-
import os
import jieba
import io


#用于去除符号和空格。

# 也完成了分词  注释掉了
current_path = os.path.dirname(__file__)
os.chdir(current_path)

def text_to_wordlist(text):
    words = list(jieba.cut(text.strip(), cut_all=False))
    return (" ".join(words))

def eachLine(fileName,fileName2):
        with io.open(fileName, "r", encoding='utf-8') as f:
            lines = f.readlines()

        with io.open(fileName2, "w", encoding='utf-8') as f_w:
            for line in lines:
                strline = line.strip()\
                    .replace('。','').strip(':').strip('.').strip(';').replace(' ','').replace('\t', '').replace('\u3000', '').replace('<','').replace('>','').replace('(','')\
                    .replace(')','').replace('?','').replace('，','').replace('　　　　　','')\
                    .replace('：','').replace('　','').replace('“','').replace('”','').replace('.','').replace('、','').replace('》','').replace('《','')\
                    .replace('（','').replace('）','').replace(']','').replace('[','').replace('］','').replace('［','').replace('；','')
                print (strline)

                # print (text_to_wordlist(strline))

                # f_w.write(text_to_wordlist(strline)+'\n')
                f_w.write(strline + '\n')


if __name__ == '__main__':
    # 需要去空格的文件
    fileName="../../data/tmp2/oneline.txt"
    # 输出文件
    fileName2="../../data/tmp2/nosign.txt"
    eachLine(fileName,fileName2)


