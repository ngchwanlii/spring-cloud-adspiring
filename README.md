

# Setup Eureka Server
- Follow [Setup Eureka Server](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-eureka-server.html) documentation
- Setup peer awareness
- If there are some error while running eureka-server, try ``` mvn clean package -Dmaven.test.skip=true -U```
- For ```git backend```, see [Spring Cloud Config](https://cloud.spring.io/spring-cloud-static/spring-cloud-config/1.2.3.RELEASE/)
- For ```file system backend```, ```cd ad-eureka/target``` run ```java -jar ad-eureka-1.0-SNAPSHOT.jar --spring.profiles.active=<your-peer-servers>```

# API-Gateway (Zuul)
- For details information, read the documentation / links from [zuul](https://github.com/Netflix/zuul)   


# Task that has build
- ad-gateway
    - filter: 
        - for logging system