# Base image
FROM openjdk:17-jdk-alpine

# Uygulama dosyalarını imaj içine kopyalama
COPY target/*.jar /app/app.jar

# Çalışma dizinini ayarlama
WORKDIR /app

# Uygulamayı başlatma komutu
CMD ["java", "-jar", "app.jar"]