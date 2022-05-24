FROM ubuntu:20.04

RUN apt update && apt install -y wget
WORKDIR /root

RUN apt install -y default-jre
RUN apt install -y default-jdk
RUN apt install -y git
RUN git clone https://github.com/yinpeiqi/cs324-distributed.git

WORKDIR /root/cs324-distributed/MyRMI
RUN javac -d target/ src/main/java/myrmi/*/*.java src/main/java/myrmi/Remote.java
CMD ["bash", "/root/cs324-distributed/HW4/entry.sh"]