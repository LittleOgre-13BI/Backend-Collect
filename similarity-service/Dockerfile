FROM python:3.6
ADD requirements.txt requirements.txt
RUN pip install -r requirements.txt -i https://mirrors.aliyun.com/pypi/simple
EXPOSE 5000
ADD run.sh run.sh
RUN chmod +x run.sh
CMD cd similarity-service && python app.py