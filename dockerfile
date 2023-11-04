# 使用 adoptopenjdk 11 版本的基础镜像
FROM openjdk:11.0-jre-buster

# 拷贝应用程序代码到容器中
WORKDIR /app
COPY user-center-0.0.1-SNAPSHOT.jar ./app.jar

# 构建应用程序

# 运行应用程序
CMD ["java", "-jar", "/app/app.jar","--spring.profiles.active=prod"]
