# 导入 Flask 类
import json

from flask import Flask, request, jsonify, make_response
import solver

# 创建了这个类的实例。第一个参数是应用模块或者包的名称。
app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False
app.config['JSONIFY_MIMETYPE'] = "application/json;charset=utf-8"
agent0=solver.Agent()

# 使用 route() 装饰器来告诉 Flask 触发函数的 URL
# nohup python3 myApp.py >> ./log/flask.log 2>&1 &
@app.route("/api/event", methods=['POST'])
def predict():
    if request.method == 'POST':
        #Event = request.args.get('Event', '')
        data=request.get_json()
        #data = json.loads(data)
        Event=data.get('Event')
        print(Event)
        if Event in ['pickUpItemEvent','breakBlockEvent']:
            return jsonify({})


        # Emotion=data.get('Emotion')
        agent0.prase(data)
        ans=0
        # print(agent0.emotions)
        res=agent0.solve()
        # ans=agent0.explain(res)
        # print(ans)
        emo=agent0.getEmotion(Event,data['State'])
        if len(emo)>0:
            emo=emo[0]
        else:

            return jsonify({})
        if Event.find('Attack')>0 or Event.find('Monster')>0:
            Event='fight,alive'
        elif emo=='hopeful' or emo=='joyful':
            Event = 'openB,food'
        print(agent0.clauses)

        return jsonify( str(emo)+'('+Event+')')


@app.route("/api/answer", methods=['GET'])
def display():
    if request.method == 'GET':
        global legal_answer
        res = make_response(jsonify({'answer': legal_answer}))
        res.status = '200'  # 设置状态码
        res.headers['Access-Control-Allow-Origin'] = "*"  # 设置允许跨域
        res.headers['Access-Control-Allow-Methods'] = 'PUT,GET,POST,DELETE'
        return res

if __name__ == "__main__":
    # 使用 run() 函数来运行本地服务器和我们的应用
    app.run("0.0.0.0", 5000)
