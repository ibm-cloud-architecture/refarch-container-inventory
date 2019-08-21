from ibmcom/mq

USER root
RUN useradd alice -G mqm && \
    echo alice:passw0rd | chpasswd
USER mqm

COPY 20-config.mqsc /etc/mqm/