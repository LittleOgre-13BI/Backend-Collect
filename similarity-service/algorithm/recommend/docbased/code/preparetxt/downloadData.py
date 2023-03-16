import os
from urllib import request

#这两句用于处理main中os调用的错误。
current_path = os.path.dirname(__file__)
os.chdir(current_path)



data={}
def load_file(filepath):
    with open(filepath, 'r') as f:
        for i, line in enumerate(f):
            if i == 0:
                continue
            yield line.strip('\r\n')

def download(filepath,outpath):
    for line in load_file(filepath):
        mid, url= line.split(',')
        data[mid] = url
        request.urlretrieve(url, filename=outpath+"/"+mid+".txt")


if __name__ == '__main__':
    filepath1 = "../../data/allDocsDescription.csv"
    filepath2 = "../../data/doneDocsDescription.csv"
    outpath1="../../data/allDocsDescription"
    outpath2="../../data/doneDocsDescription"
    if not os.path.exists(outpath1):
        os.makedirs(outpath1)
    if not os.path.exists(outpath2):
        os.makedirs(outpath2)
    download(filepath1,outpath1)
    download(filepath2,outpath2)
    print("开始下载数据")
    print("数据下载成功")