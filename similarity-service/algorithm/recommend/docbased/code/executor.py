import os
import shutil

from .tfidf import tfexcute

def excute():
    #这两句用于处理main中os调用的错误。
    current_path = os.path.dirname(__file__)
    os.chdir(current_path)


    #如果原文件地址存在，就先将源文件夹删除
    if os.path.exists("../data/allDocsDescription"):
        print("存在全部任务描述文件夹，正在删除...")
        shutil.rmtree("../data/allDocsDescription")
        print("全部任务描述文件夹删除成功")
    if os.path.exists("../data/doneDocsDescription"):
        print("存在已完成任务描述文件夹")
        shutil.rmtree("../data/doneDocsDescription")
        print("已完成任务任务文件夹删除成功")
    #把文件夹都创建出来
    if not os.path.exists("../data/tmp"):
        os.mkdir("../data/tmp")
    if not os.path.exists("../data/tmp2"):
        os.mkdir("../data/tmp2")
    #按照csv下载数据到两个文件夹中
    os.system("python ./preparetxt/downloadData.py")

    #去文件夹符号
    os.system("python ./preparetxt/folderremovesign.py")
    #流程1：转一行
    os.system("python ./preparetxt/totext.py")
    #流程2：去符号
    os.system("python ./preparetxt/removesign.py")
    #流程3：分开
    os.system("python ./preparetxt/split.py")
    #预测1
    os.system("python ./trainw2v.py")
    #预测2
    os.system("python ./trainembedding.py")
    #预测3
    result=tfexcute()
    return result
# result=excute()
# print(result)