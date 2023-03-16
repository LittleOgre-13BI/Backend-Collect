import csv
import os

from db.fetchmission import *
from algorithm.similarity.code.executor import excute

def combine(content):
    string = []
    for i in range(4):
        if content[i] is None:
            string.append("")
        else:
            string.append(content[i])

    result = "故障描述：" + string[1] + "复现步骤：" + string[2] + "设备信息：" + string[3]
    return result

def build_csv(fid, mid):
    #这两句用于处理main中os调用的错误。
    current_path = os.path.dirname(__file__)
    os.chdir(current_path)
    print(os.getcwd())

    contents = search_all_contents(mid)
    print(os.getcwd())
    file_name = '../algorithm/similarity/data/thisReport.csv'
    with open(file_name, 'w', newline='', encoding='utf-8') as file:
        writer = csv.writer(file)
        writer.writerow(['rid', 'content'])
        for content in contents:
            if str(content[0]) == fid:
                writer.writerow([content[0], combine(content)])

    file_name = '../algorithm/similarity/data/reports.csv'
    with open(file_name, 'w', newline='', encoding='utf-8') as file:
        writer = csv.writer(file)
        writer.writerow(['rid', 'content'])
        for content in contents:
            if str(content[0])  != fid:
                writer.writerow([content[0], combine(content)])

def similarity_one(fid, mid):
    build_csv(fid, mid)
    return excute()

# print(similarity_one(1, 1))


