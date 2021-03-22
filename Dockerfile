FROM clojure:latest

WORKDIR /hello

ADD . /hello

RUN lein deps

EXPOSE 8080

CMD ["lein", "run"]
