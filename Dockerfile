FROM amazoncorretto:17
COPY build/install/soccerTime2 /app/soccerTime2
WORKDIR /app/soccerTime2/bin
EXPOSE 9000
ENTRYPOINT ./soccerTime2