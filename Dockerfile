FROM amazoncorretto:17.0.12

# set a directory for the app
RUN mkdir /app

# copy jar to the container
COPY build/libs/audition-api-0.0.1-SNAPSHOT.jar /app/audition-api.jar

# define the port number the container should expose
EXPOSE 8080

WORKDIR /app

# run the command
CMD "java" "-jar" "audition-api.jar"
