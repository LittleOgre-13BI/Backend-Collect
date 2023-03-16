#用于整个文件夹所有文件去符号。
import io
import os
import re

#这两句用于处理main中os调用的错误。
current_path = os.path.dirname(__file__)
os.chdir(current_path)


dirname = '../../data/allDocsDescription'
files=os.listdir(dirname)
for eachfile in files:
    filename=dirname+'/'+eachfile
    content=""
    #只保留中文，英文，数字
    with io.open(filename, 'r', encoding='utf-8') as f:
        cop = re.compile("[^\u4e00-\u9fa5^a-z^A-Z^0-9]")
        content=cop.sub('',f.read())

    with io.open(filename,'w',encoding='utf-8')as f:
        f.write(content)
print("allDocsDescription字符处理成功")