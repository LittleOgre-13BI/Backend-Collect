from flask import Flask
from controller.algrithm import algrithm
from gevent import pywsgi

app = Flask(__name__)
app.register_blueprint(algrithm)


@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'


if __name__ == '__main__':
    server = pywsgi.WSGIServer(('0.0.0.0', 5000), app)
    server.serve_forever()
