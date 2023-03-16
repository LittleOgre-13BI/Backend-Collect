import os
from urllib import request

#这两句用于处理main中os调用的错误。
current_path = os.path.dirname(__file__)
os.chdir(current_path)



data={}
def load_file(filepath):
    with open(filepath, 'r',encoding='utf-8') as f:
        for i, line in enumerate(f):
            if i == 0:
                continue
            yield line.strip('\r\n')

def load(filepath,outpath):
    for line in load_file(filepath):
        rid, content= line.split(',')
        data[rid] = content
        with open(outpath+"/"+str(rid)+".txt",'w',encoding='utf-8')as f:
            f.write(content)



if __name__ == '__main__':
    filepath1 = "../../data/thisReport.csv"
    filepath2 = "../../data/reports.csv"
    outpath1="../../data/thisReport"
    outpath2="../../data/reports"
    if not os.path.exists(outpath1):
        os.makedirs(outpath1)
    if not os.path.exists(outpath2):
        os.makedirs(outpath2)
    print("开始加载数据")
    load(filepath1,outpath1)
    load(filepath2,outpath2)
    print("数据加载成功")