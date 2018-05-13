```
java -jar naivechain.jar 8080 7000
java -jar naivechain.jar 8081 7001
java -jar naivechain.jar 8082 7002

curl --data "peer=ws://localhost:7001" http://localhost:8080/addPeer
curl --data "peer=ws://localhost:7002" http://localhost:8080/addPeer

curl http://localhost:8080/peers
curl http://localhost:8080/blocks
curl http://localhost:8080/miner
```