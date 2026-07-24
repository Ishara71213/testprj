netstat -ano | findstr :8081
tasklist | findstr 3992 

taskkill /PID 3992 /F

# Docker commands
docker network create
docker network inspect test-network

docker rm 
docker rmi
docker ps -a
docker images

docker run -d -p 3345:3345 --name mysql-db -e MYSQL_ROOT_PASSWORD=1234 --net test-network mysql:oraclelinux9 --port 3345
docker logs mysql-db

docker run -d -p 8081:8081 --name testprj --net test-network testprj:1.0
docker exec -it testprj /bin/bash

docker run -d -p 8081:8081 -e DB_URL=jdbc:mysql://mysql-db:3345/testprj_db -e DB_USERNAME=root -e DB_PASSWORD=1234 --name testprj --net test-network testprj:1.0

# Run docker image
