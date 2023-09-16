from pysat.solvers import Glucose3

class Agent:
    def __init__(self):
        self.solver = Glucose3()
        self.emotions=[]
        self.status=set()
        self.clauses=[]
        self.codeBook={'Bread':1,'Wooden Sword':0,'Apple':2,'Cookie':3}
        self.decodeBook={1:'Bread',0:'Wooden Sword',2:'Apple',3:'Cookie'}
        self.groundTrue={'joyful':{13},'distressed':{2},'fearful':{2},'hopeful':{13}}
    def getFutureDiff(self,event):
        statusDiff=set()

        if event=='watchChestEvent':
            for i in range(len(self.codeBook)):
                statusDiff.add(6+1+2*i)

        elif event=='encounterMonsterEvent':
            statusDiff.add(2)

        return statusDiff

    def code(self,obj,flag):#自动给物品编码,flag=1是增加，flag=2是减少
        status = set()
        for key in obj:
            key = key[1:-1]  # 删除key的括号
            if key not in self.codeBook:
                self.codeBook[key] = len(self.codeBook) #从0开始给物品编码
                self.decodeBook[len(self.decodeBook)]=key #加入解码本
                id=len(self.codeBook)-1
                getid=6+id*2+1
                lossid=getid+1
                lossid=getid+1
                self.clauses.append([-getid,-lossid])#加入子句,防止冲突
            status.add(2*self.codeBook[key]+flag+6)#前三位给状态编码，从第7开始给物品编码
        return status

    def diff(self,category, state, k):
        category2 = 'Previous' + category
        if state[category]*state[category2]<=0:
            if state[category] > state[category2]:
                self.status.add(k)
            elif state[category2] > state[category]:
                self.status.add(k + 1)

    def deleteElements(self,flag):#flag=1 增加 flag=0 减少 #分离状态
        #删除self.status的所有奇数
        status=set()
        for item in self.status:
            if item%2==flag:
                status.add(item)
        self.status=status
    def getEmotion(self,evt,state):
        current=set(self.status)
        emotions=[]
        for item in ['joyful','distressed']:
            if len(self.groundTrue[item]&current)>0:
                emotions.append(item)
        for item in ['hopeful','fearful']:
            if len(self.getFutureDiff(evt)&self.groundTrue[item])>0:
                emotions.append(item)
        if len(current)>0 and len(emotions)==0:
            return ['neutral']
        if state['Health'] >= 14 and emotions.count('hopeful') > 0:
            emotions.remove('hopeful')  # 防止高血量期待
        return emotions

    def prase(self,obj):

        self.status = set()
        # emo = obj['Emotion']
        evt = obj['Event']
        # 1 health+ 2 health-
        # 3 Armor+ 4 Armor-
        # 5 Food+ 6 Food-
        state = obj['State']
        self.diff('Health', state, 1)
        # self.diff('ArmorValue', state, 3)
        # self.diff('FoodLevel', state, 5)
        # 写一个代码，计算PreviousInventory和Inventory的差异,区分Inventory增加的和减少的
        addObjects = {}  # 增加的部分
        deleteObjects = {}  # 减少的部分
        self.code(set(state['Inventory'][0].keys()), 1)  # 刷新字典
        state['Inventory'] = state['Inventory'][0]
        state['PreviousInventory'] = state['PreviousInventory'][0]  # 解耦
        for key in state['Inventory']:
            if key not in state['PreviousInventory']:
                addObjects[key] = state['Inventory'][key]
            else:
                # 比较数量，看看是不是变多了
                if state['Inventory'][key] > state['PreviousInventory'][key]:
                    addObjects[key] = state['Inventory'][key] - state['PreviousInventory'][key]
        for key in state['PreviousInventory']:
            if key not in state['Inventory']:
                deleteObjects[key] = state['PreviousInventory'][key]
            else:
                # 比较数量，看看是不是变少了
                if state['Inventory'][key] < state['PreviousInventory'][key]:
                    deleteObjects[key] = -state['Inventory'][key] + state['PreviousInventory'][key]

        self.status = self.status.union(self.code(addObjects, 1).union(self.code(deleteObjects, 2)))
        print(self.status)
        emos=self.getEmotion(evt,state)
        print(state['Health'])

        for emo in emos:
            self.add_emotion(self.status, emo, evt)

    def add_emotion(self,status,emotion,event):
        if emotion=='joyful':

            self.status=status
            self.emotions.append((status,'joyful'))
            self.deleteElements(1)
            self.clauses.append(list(self.status))
        elif emotion=='distressed':

            self.status=status
            self.emotions.append((status,'distressed'))
            self.deleteElements(0)
            self.clauses.append(list(self.status))

        elif emotion=='hopeful':
            self.status = status
            statusDiff=self.getFutureDiff(event)
            if len(statusDiff)>0:
                self.emotions.append((statusDiff,'hopeful'))
                self.clauses.append(list(statusDiff))

        elif emotion=='fearful':
            if event=='watchChestEvent':#不能在宝箱前害怕
                return
            self.status = status
            statusDiff = self.getFutureDiff(event)
            if len(statusDiff) > 0:
                self.emotions.append((statusDiff, 'fearful'))
                self.clauses.append(list(statusDiff))

        elif emotion=='neutral':
            self.status=status
            self.emotions.append((status,'neutral'))
            for item in status:
                self.clauses.append([-item])
        else:
            print('Invalid emotion')
    def explain(self,book):
        if book==None:
            return 'No solution'
        praseDict={1:'Health',2:'Health',3:'Armor',4:'Armor',5:'Food',6:'Food'}
        result=''
        for item in book:
            if item>1 and item<=6:#trick
                result+='Care '+praseDict[item]+' '
            elif item>6:
                z=(item-7)//2
                flag=item%2
                if flag==1:
                    result+='Want '+self.decodeBook[z]+' '
                # else:
                #     result+='Hate- '+self.decodeBook[z]+' '
        return result

    def solve(self):
        for clause in self.clauses:
            if len(clause)>0:
                self.solver.add_clause(clause)
        if self.solver.solve():

            return self.solver.get_model()
        else:
            return None
