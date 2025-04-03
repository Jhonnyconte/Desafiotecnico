# Desafio Técnico - CRUD de Dispositivos

## Requisitos para Rodar a Aplicação

Para rodar a aplicação, basta ter o **Docker** instalado e executar o **docker-compose**. Alternativamente, a aplicação pode ser executada localmente pelo IntelliJ ou empacotada em um container separado.

---

## 🐳 Rodando com Docker-Compose (Recomendado)
A maneira mais simples de rodar o projeto é utilizando **Docker Compose**, pois ele sobe automaticamente a aplicação junto com o **PostgreSQL** e o **RabbitMQ**.

```sh
# Certifique-se de estar na raiz do projeto e rode:
docker-compose up -d
```

Isso criará e iniciará os containers para:
- **PostgreSQL** (banco de dados)
- **RabbitMQ** (mensageria)
- **Aplicação** (API Java Spring Boot)

Para verificar se tudo subiu corretamente, use:
```sh
docker ps
```

A API estará disponível em: [http://localhost:8080](http://localhost:8080)

---

## 🔧 Rodando Localmente pelo IntelliJ
Caso queira rodar a aplicação localmente pelo **IntelliJ**, siga os passos abaixo:

1. **Compile o projeto com o JDK Amazon Corretto 17**
    ```sh
    mvn clean install (Precisa das variaveis RabbitMQ para rodar os testes)
   ou
   mvn clean install -DskipTests
    ```
   
2. **Comente a seção `app` no `docker-compose.yml`** para evitar que o Docker tente subir a aplicação automaticamente:
    ```yaml
    # app:
    #   build: .
    #   container_name: desafio-tecnico
    #   restart: always
    #   depends_on:
    #     postgres:
    #       condition: service_healthy
    #     rabbitmq:
    #       condition: service_started
    #   ports:
    #      - "8080:8080"
    #   environment:
    #     - DB_HOST=postgres
    #     - DB_NAME=desafio
    #     - DB_USER=jhonny
    #     - DB_PASSWORD=secret
    #     - DB_PORT=5432
    ```
3. **Rode a aplicação pelo IntelliJ** (classe `DeviceCrudApplication` no package `com.example.devicecrud`).

4. **(Opcional) Caso ao rodar não suba o docker do banco e do rabbitmq:**
    ```sh
    docker-compose up -d postgres rabbitmq
    ```
---

## 🏗️ Criando um Container Único para a Aplicação
Caso queira rodar a aplicação como um container separado (sem usar `docker-compose` para ela), siga estes passos:

1. **Compile o projeto e gere o pacote JAR:**
    ```sh
    mvn clean package
    ```
2. **Crie a imagem Docker da aplicação:**
    ```sh
    docker build -t desafio-tecnico .
    ```
3. **Rode o container apontando para um banco e RabbitMQ existentes:**
   ```sh
    docker run -d --name desafio-app -p 8080:8080 \
        -e DB_HOST=localhost \
        -e DB_NAME=desafio \
        -e DB_PASSWORD=secret \
        -e DB_PORT=5432 \
        -e DB_USER=jhonny \
        -e RABBITMQ_HOST=localhost \
        -e RABBITMQ_PORT=5672 \
        -e RABBITMQ_USER=admin \
        -e RABBITMQ_PASSWORD=admin \  
        -e RABBITMQ_QUEUE=deviceQueue \
   desafio-tecnico
    ```

A API estará disponível em: [http://localhost:8080](http://localhost:8080)

---

## 🚀 Conclusão
- Para **rodar tudo com Docker**, basta executar `docker-compose up -d`.
- Para **rodar localmente pelo IntelliJ**, comente a seção `app` do `docker-compose.yml`,e caso necessario rode `docker-compose up -d postgres rabbitmq`, e inicie a aplicação pelo IntelliJ.
- Para **rodar a aplicação em um container separado**, compile o projeto (`mvn package`), crie a imagem Docker (`docker build`), e rode o container apontando para o banco e RabbitMQ existentes.


