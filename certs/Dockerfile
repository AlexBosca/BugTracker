FROM alpine:3.19

RUN apk add --no-cache openssl bash

WORKDIR /certs
COPY generate-certs.sh .
RUN chmod +x generate-certs.sh

CMD ["./generate-certs.sh"]
