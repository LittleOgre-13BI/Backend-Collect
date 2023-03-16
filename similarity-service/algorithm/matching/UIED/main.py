import os
import shutil
from urllib import request
from .similarity_calculation import cal_total_sim, Position
from .run_single import run_single

def execute(description, url, column_min, column_max, row_min, row_max, lda_news):
    #这两句用于处理main中os调用的错误。
    current_path = os.path.dirname(__file__)
    os.chdir(current_path)

    targetPosition = Position(column_min,column_max,row_min,row_max)
    #如果原文件地址存在，就先将源文件夹删除
    if os.path.exists("data/input"):
        print("存在data/input文件夹，正在删除...")
        shutil.rmtree("data/input")
        print("data/input文件夹删除成功")
    if os.path.exists("data/output/ip"):
        print("存在data/out/ip文件夹，正在删除...")
        shutil.rmtree("data/output/ip")
        print("data/out/ip文件夹删除成功")
    if os.path.exists("data/output/merge"):
        print("存在data/out/merge文件夹，正在删除...")
        shutil.rmtree("data/output/merge")
        print("data/out/merge文件夹删除成功")
    if os.path.exists("data/output/ocr"):
        print("存在data/out/ocr文件夹，正在删除...")
        shutil.rmtree("data/output/ocr")
        print("data/out/ocr文件夹删除成功")
    #把文件夹都创建出来
    if not os.path.exists("data/input"):
        os.mkdir("data/input")
    if not os.path.exists("data/output/ip"):
        os.mkdir("data/output/ip")
    if not os.path.exists("data/output/merge"):
        os.mkdir("data/output/merge")
    if not os.path.exists("data/output/ocr"):
        os.mkdir("data/output/ocr")
    #下载图片
    request.urlretrieve(url, filename="data/input/picture.png")
    # 设置图片路径
    input_path_img = 'data/input/picture.png'
    output_root = 'data/output'
    run_single(input_path_img, output_root)
    jsonurl = output_root+'/merge/picture.json'
    similarity = cal_total_sim(description, jsonurl=jsonurl, targetPosition=targetPosition, lda_news=lda_news)
    return similarity


# print(execute("1.点击“我的”按钮 2.点击右上角设置图标 3.点击“个人信息”；4.点击“出生日期”选择2017.10.21 预期输出：步骤4后系统提示出生日期不正确，不合常理； 实际输出：能够正常的保存完善个人信息；",
#               "http://mooctest-site.oss-cn-shanghai.aliyuncs.com/app/1508582741034/%E6%88%AA%E5%B1%8F_20171021_184147.jpg",
#             0,109,300,600))
