# Sample Application for article

- [article1](https://zenn.dev/kiyotatakeshi/articles/fc593c768ad7e0)
- [article2](https://zenn.dev/kiyotatakeshi/articles/cf198ab5e6c735)
- [article3](https://zenn.dev/kiyotatakeshi/articles/73f722f99b7bf5)
- [article4](https://zenn.dev/kiyotatakeshi/articles/d12a850f232d71)

## requirement

- kotlin(1.6),java(17)
    - use [asdf](https://asdf-vm.com/guide/getting-started.html) and [asdf-java](https://github.com/halcyon/asdf-java), [asdf-kotlin](https://github.com/asdf-community/asdf-kotlin) plugin

```shell
asdf install java liberica-17.0.3.1+2

asdf install kotlin 1.6.21

$ kotlinc -version
info: kotlinc-jvm 1.6.21 (JRE 17.0.3.1+2-LTS)

$ java -version                                    
openjdk version "17.0.3.1" 2022-04-22 LTS
OpenJDK Runtime Environment (build 17.0.3.1+2-LTS)
OpenJDK 64-Bit Server VM (build 17.0.3.1+2-LTS, mixed mode, sharing)
```

## generate rsa public and private key for self-signed JWT 

[how to generate](./src/main/resources/certs/memo.md)

## setup DB

- run Postgres

```shell
$ docker compose up -d
```

- use [Makefile](./Makefile)

```shell
$ make init-db show-sample-data
```

## generate code for jOOQ

```shell
$ make generate-jooq-code
```

## test API

- run application

```shell
$ make all
```

- [use Postman collections](./postman/spring-security-zenn-ariticle.postman_collection.json)
