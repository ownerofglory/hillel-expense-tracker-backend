# Build

### Prerequisites

For the dev machine:
- JDK version >= 23
- Maven (optional, Maven wrapper can be used instead)
- Docker
- Helm (optional)
- kubectl (optional)
- MySQL (local, remote ot container)


### Execute build
```shell
    ./mvnw clean package
```

You can skipt test execution (recommened only for debugging)
```shell
    ./mvnw clean package -Dmaven.test.skipt=true
```

### Run locally

```shell
    JDBC_DB_HOST=<db_host>:<db_port> \
    JDBC_DB_NAME=<db_name> \
    JDBC_PASSWORD=<db_password> \
    JDBC_USER=<db_user> \
    MYSQL_DB_NAME=<db_name> \
    MYSQL_PASSWORD=<db_password> \
    MYSQL_USER=<db_user>\
    OPENAI_ENDPOINT=https://api.openai.com/v1/chat \
    OPENAI_KEY=<openai_key> \
    JWT_KEY=<jwt_key> \
    java  -jar hillel-expense-tracker-web-boot/target/hillel-expense-tracker-web-boot-1.0-SNAPSHOT.jar
```

### Build Docker image
#### Build an image
```shell
    docker build -t hillel-expense-tracker .
```
#### Tag the image
```shell
    docker image tag  hillel-expense-tracker:<tag> <docker_repo>/hillel-expense-tracker:<tag>
```

#### Push the image into Docker Hub
```shell
  docker push <docker_repo>/hillel-expense-tracker:<tag>
```

