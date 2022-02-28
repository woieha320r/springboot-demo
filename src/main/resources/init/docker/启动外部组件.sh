# MariaDB。
docker run -d \
  -p 3310:3306 \
  -e MYSQL_ROOT_PASSWORD=0a!root9z+ \
  --name mariadb mariadb:10.6.5;
# Redis。密码可以进入后config set
docker run -d \
  -p 6380:6379 \
  --name redis redis:6.0.16;
# ActiveMQ5。8161是网页管理端口，密码不能有特殊字符
docker run -d \
  -p 8170:8161 \
  -p 61620:61616 \
  -e ACTIVEMQ_ADMIN_LOGIN=root \
  -e ACTIVEMQ_ADMIN_PASSWORD=0aroot9z \
  --name activemq5 webcenter/activemq:5.14.3;
