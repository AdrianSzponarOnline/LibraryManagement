FROM openjdk:22-jdk
LABEL authors="Adrian"
#katalog roboczy
WORKDIR /app

#kopiowanie pliku JAR do obrazu
COPY target/LibraryManagement-0.0.1-SNAPSHOT.jar app.jar
#port
EXPOSE 8080

#Polecenie uruchomiające aplikację
ENTRYPOINT ["java", "-jar", "app.jar"]


ENTRYPOINT ["top", "-b"]