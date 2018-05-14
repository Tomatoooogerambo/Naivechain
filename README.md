# Naivechain
#### Follow to learn Blockchain implementation.<br> 
#### naivechain with Java

### 用于学习区块链的底层原理
1. lhartikk提供的构建区块链的思路： [动手之前先理清楚思路吧！](https://lhartikk.github.io/)<br>
2. 如果理解有难度，配合着视频讲解也不错哦！ [讲解配套第一点的视频](https://github.com/Fabsqrt/BitTigerLab/blob/master/Blockchain/Classes/Blockchain/README.md)<br>
3. js写的java就写不出来？ 不要被编程语言绊住， 它只是你和计算机交谈的一种方式。[java实现参考版本](https://github.com/sunysen/naivechain)
或者查看本demo

# Quick Start
```
git clone git@github.com:Tomatoooogerambo/Naivechain.git
```

cd 到clone的目录下
```
cd naivechain
```

```
mvn install
```

cd到target
```
cd target
```

打开多个命令窗口，用于模仿多个节点。<br>
在每一个命令窗口分别打开
#### 注意 在生成的target目录下打开窗口
```
java -jar naivechain.jar 8080 7000
java -jar naivechain.jar 8081 7001
java -jar naivechain.jar 8082 7002
```

现在我们有3个节点了<br>
再打开一个命令窗口，开始测试各功能<br>
_windows下如果调用curl需要安装先。当然你也可以使用其他的命令调用_<br>
_这里给出curl的[安装教程](https://blog.csdn.net/zoubf/article/details/51809967)_
1. 先来看下自己的初始创世块的信息
```
curl http://localhost:8080/blocks
```

2. 再来链接一下各个节点
```
curl --data "peer=ws://localhost:7001" http://localhost:8080/addPeer
curl --data "peer=ws://localhost:7002" http://localhost:8080/addPeer
```

3. 查看一下peers
```
curl http://localhost:8080/peers
```

4. 某个节点挖个矿
```
curl http://localhost:8080/miner
```

5. 查看一下挖完矿之后， 看看区块链有变化吗
```
curl http://localhost:8080/blocks
```

## 欢迎讨论交流Star
e-mail: 736170457@qq.com
 
