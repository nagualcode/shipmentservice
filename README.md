# ShippingService

ShippingService é um sistema para registro e acompanhamento de entregas que consiste em dois microsserviços, **UserService** e **TrackingService**, que interagem entre si usando **OpenFeign** para comunicação HTTP. O **UserService** gerencia usuários e os pacotes associados a eles, enquanto o **TrackingService** lida com o rastreamento de pacotes, armazenando e retornando o status de um determinado pacote. O **OpenFeign** garante que a informação do status do pacote exibida para o usuário seja sempre atualizada quando consultada via UserService.

## Tecnologias Utilizadas

- **Spring Boot 3.3.4**: Framework principal para o desenvolvimento dos microsserviços.
- **OpenFeign**: Cliente HTTP declarativo utilizado para a comunicação entre serviços.
- **PostgreSQL**: Banco de dados relacional para armazenar informações de usuários e pacotes.
- **Docker e Kubernetes**: Ferramentas utilizadas para a containerização e orquestração dos microsserviços.
- **Flyway**: Gerenciamento de migrações de banco de dados.
- **JUnit, MockMvc e Testcontainers**: Utilizados para testes unitários e de integração.

## Funcionamento do OpenFeign

O **UserService** possui uma entidade `Package`, e as informações sobre o status de um pacote são obtidas dinamicamente através de uma chamada ao **TrackingService** via **OpenFeign** quando o usuário consulta os detalhes de seus pacotes. O método no controlador do **UserService** faz essa chamada HTTP ao **TrackingService** para garantir que o status do pacote esteja sempre atualizado.

### Diagrama de Sequência

![Diagrama de Sequência](sequence.png)

Essa abordagem segue o princípio de separação de responsabilidades, onde cada microsserviço se concentra em uma funcionalidade específica (o **UserService** gerencia usuários e o **TrackingService** gerencia o rastreamento). A modularidade facilita a manutenção, escalabilidade e evolução da aplicação.

## Orquestração com Kubernetes

Os microsserviços são containerizados usando **Docker** e orquestrados com **Kubernetes**. Cada serviço possui seus próprios deployments e services. O **Flyway** é utilizado para gerenciar e garantir a consistência do esquema do banco de dados tanto no **UserService** quanto no **TrackingService**.

## Estrutura do Banco de Dados

Abaixo estão os esquemas utilizados para o **UserService** e o **TrackingService**, gerenciados pelo **Flyway**.

### Esquema do **UserService**

```sql
CREATE SCHEMA IF NOT EXISTS userservice;

CREATE TABLE IF NOT EXISTS userservice.users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS userservice.packages (
    id SERIAL PRIMARY KEY,
    tracking_number VARCHAR(255) NOT NULL UNIQUE,
    user_id INTEGER REFERENCES userservice.users(id)
);
```

### Esquema do **TrackingService**

```sql
CREATE SCHEMA IF NOT EXISTS trackingservice;

CREATE TABLE IF NOT EXISTS trackingservice.packages (
    id SERIAL PRIMARY KEY,
    tracking_number VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(255) NOT NULL
);
```

## Iniciando o Sistema com Kubernetes

### Pré-requisitos

- **Docker**: Para construir as imagens Docker.
- **Kubernetes (Minikube ou Docker Desktop)**: Para orquestração dos microsserviços.
- **kubectl**: Ferramenta para interagir com o cluster Kubernetes.

### Passos

1. **Build das Imagens Docker**:
   - No diretório de cada microsserviço, execute os comandos abaixo para criar as imagens:

   ```bash
   docker build -t userservice:latest .
   docker build -t trackingservice:latest .
   ```

2. **Iniciar o Cluster Kubernetes**:
   - Inicie o cluster local do Kubernetes usando Minikube:

   ```bash
   minikube start
   ```

3. **Implantar os Microsserviços**:
   - No diretório de cada microsserviço, aplique os manifests do Kubernetes para criar os deployments e serviços:

   ```bash
   kubectl apply -f k8s/userservice-deployment.yaml
   kubectl apply -f k8s/trackingservice-deployment.yaml
   ```

4. **Configurar o PostgreSQL**:
   - Aplique o manifest para configurar o banco de dados PostgreSQL no Kubernetes:

   ```bash
   kubectl apply -f k8s/postgres-deployment.yaml
   ```

5. **Acessar os Serviços**:
   - Verifique os serviços criados usando o comando `kubectl get services` e utilize o Minikube para acessá-los:

   ```bash
   minikube service userservice
   minikube service trackingservice
   ```

## Comandos CURL para Testes

### Criar um usuário

```bash
curl -X POST http://localhost:8081/users \
-H "Content-Type: application/json" \
-d '{"email": "test@example.com"}'
```

### Adicionar um pacote a um usuário

```bash
curl -X POST http://localhost:8081/users/1/packages \
-H "Content-Type: application/json" \
-d '{"trackingNumber": "123ABC"}'
```

### Criar um pacote no **TrackingService**

```bash
curl -X POST http://localhost:8082/packages \
-H "Content-Type: application/json" \
-d '{"trackingNumber": "123ABC", "status": "in transit"}'
```

### Atualizar o status de um pacote no **TrackingService**

```bash
curl -X PUT http://localhost:8082/packages/123ABC \
-H "Content-Type: application/json" \
-d '{"status": "shipped"}'
```

### Consultar as informações de um usuário e seus pacotes

Essa chamada ao **UserService** acionará o controlador, que por sua vez utilizará o **OpenFeign** para buscar o status do pacote diretamente do **TrackingService**.

```bash
curl -X GET http://localhost:8081/users/1
```

## Testes Automatizados

A aplicação utiliza **JUnit**, **MockMvc**, e **Testcontainers** para garantir a qualidade do código e verificar as interações entre os microsserviços. O uso de **Testcontainers** permite que os testes sejam executados com uma instância real do PostgreSQL rodando dentro de containers.

## Licença

Este projeto está licenciado sob a [Creative Commons Zero v1.0 Universal (CC0 1.0)](https://creativecommons.org/publicdomain/zero/1.0/), permitindo total liberdade para copiar, modificar, distribuir e usar o conteúdo para qualquer propósito, sem restrições. Não é necessário dar atribuição ou obter permissão.

---

