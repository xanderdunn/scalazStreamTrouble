# Build: docker build --tag=$USER/scalaztest --file=./Dockerfile .
# Run: docker run --rm $USER/scalaztest bash /start.sh

FROM ubuntu:14.04

# install dev tools
RUN apt-get install -y software-properties-common

RUN add-apt-repository -y ppa:openjdk-r/ppa
RUN add-apt-repository -y ppa:ubuntu-toolchain-r/test

RUN apt-get update && apt-get install -y openjdk-8-jdk curl wget zip

RUN sudo echo 'deb http://www.rabbitmq.com/debian/ testing main' >> /etc/apt/sources.list
RUN wget https://www.rabbitmq.com/rabbitmq-signing-key-public.asc
RUN sudo apt-key add rabbitmq-signing-key-public.asc
RUN export DEBIAN_FRONTEND=noninteractive
RUN sudo apt-get update && sudo apt-get -y install --upgrade rabbitmq-server

WORKDIR /
ADD ./build/install ./
ADD ./start.sh ./
