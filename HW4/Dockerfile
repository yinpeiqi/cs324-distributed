FROM ubuntu:20.04

RUN apt update && apt install -y wget
WORKDIR /root

RUN apt install -y default-jre
RUN apt install -y default-jdk
RUN apt install -y git
RUN echo 34
RUN git clone https://github.com/yinpeiqi/cs324-distributed.git
WORKDIR /root/cs324-distributed/HW4
RUN javac -d target/ src/main/java/*/*/*.java src/main/java/*/*.java
CMD ["bash", "/root/cs324-distributed/HW4/entry.sh"]