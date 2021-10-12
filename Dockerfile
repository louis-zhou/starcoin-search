FROM openjdk:8-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
COPY settings.xml .

RUN --mount=type=cache,target=/root/.m2 ./mvnw --settings settings.xml install -DskipTests

FROM openjdk:8-jdk-alpine
ARG HOSTS
ARG NETWORK
ARG BULK_SIZE
ARG STARCOIN_ES_URL
ARG STARCOIN_ES_PROTOCOL
ARG STARCOIN_ES_PORT
ARG STARCOIN_ES_USER
ARG STARCOIN_ES_PWD
ARG INDEXER_AUTO_START
ARG PROG_ARGS
ARG SWAP_API_URL
ARG SWAP_CONTRACT_ADDR
ARG DS_URL
ARG DB_USER_NAME
ARG DB_PWD

ENV HOSTS=$HOSTS
ENV NETWORK=$NETWORK
ENV BULK_SIZE=$BULK_SIZE
ENV STARCOIN_ES_URL=$STARCOIN_ES_URL
ENV STARCOIN_ES_PROTOCOL=$STARCOIN_ES_PROTOCOL
ENV STARCOIN_ES_PORT=$STARCOIN_ES_PORT
ENV STARCOIN_ES_USER=$STARCOIN_ES_USER
ENV STARCOIN_ES_PWD=$STARCOIN_ES_PWD
ENV INDEXER_AUTO_START=$INDEXER_AUTO_START
ENV PROG_ARGS=$PROG_ARGS
ENV SWAP_API_URL=$SWAP_API_URL
ENV SWAP_CONTRACT_ADDR=$SWAP_CONTRACT_ADDR
ENV DS_URL=$DS_URL
ENV DB_USER_NAME=$DB_USER_NAME
ENV DB_PWD=$DB_PWD

RUN addgroup -S starcoin && adduser -S starcoin -G starcoin
VOLUME /tmp
USER starcoin
ARG DEPENDENCY=/workspace/app/target
COPY --from=build ${DEPENDENCY}/starscan-search-1.0-SNAPSHOT.jar /app/lib/app.jar
ENTRYPOINT ["java","-noverify","-jar","app/lib/app.jar", \
            "PROG_ARGS=$PROG_ARGS", \
            "INDEXER_AUTO_START=$INDEXER_AUTO_START", \
            "HOSTS=$HOSTS", \
            "NETWORK=$NETWORK", \
            "BULK_SIZE=$BULK_SIZE", \
            "STARCOIN_ES_URL=$STARCOIN_ES_URL", \
            "STARCOIN_ES_PROTOCOL=$STARCOIN_ES_PROTOCOL", \
            "STARCOIN_ES_PORT=$STARCOIN_ES_PORT", \
            "STARCOIN_ES_USER=$STARCOIN_ES_USER", \
            "STARCOIN_ES_PWD=$STARCOIN_ES_PWD", \
            "SWAP_API_URL=$SWAP_API_URL", \
            "SWAP_CONTRACT_ADDR=$SWAP_CONTRACT_ADDR", \
            "DS_URL=$DS_URL", \
            "DB_USER_NAME=$DB_USER_NAME", \
            "DB_PWD=$DB_PWD" \
            ]