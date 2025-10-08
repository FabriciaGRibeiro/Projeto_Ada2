# E-Commerce Ada Tech API com Segurança Integrada

Este projeto consiste em uma API de E-Commerce completa, desenvolvida para a Ada Tech, com funcionalidades robustas de gestão de clientes, produtos, pedidos, cupons de desconto e regras de desconto. O sistema integra um módulo de segurança abrangente, incluindo autenticação JWT, validação de dados e processamento seguro de pagamentos. O backend é construído com Java Spring Boot e o frontend com React.

## Funcionalidades Principais

### 1. Gestão de Clientes
*   **Cadastro**: Registo de novos clientes com documento de identificação obrigatório e único, e-mail único.
*   **Listagem e Atualização**: Listar e atualizar dados de clientes.
*   **Histórico**: Clientes não são excluídos, mantendo o histórico na base de dados.

### 2. Gestão de Produtos
*   **Cadastro**: Registo de novos produtos com nome, descrição e preço.
*   **Listagem e Atualização**: Listar e atualizar dados de produtos.
*   **Status**: Produtos podem ser desativados (não excluídos) para manter o histórico.

### 3. Gestão de Pedidos
*   **Criação**: Criar novos pedidos para clientes.
*   **Itens do Pedido**: Adicionar, remover e alterar a quantidade de itens (produtos) em pedidos com status `OPEN`.
*   **Fluxo de Pedido**: Gerenciamento de status (`OPEN`, `PENDING_PAYMENT`, `PAID`, `DELIVERED`).
*   **Finalização**: Pedidos com pelo menos um item e valor total > 0 podem ser finalizados, alterando o status para `PENDING_PAYMENT` e notificando o cliente por e-mail.
*   **Pagamento**: Processamento de pagamentos para pedidos com status `PENDING_PAYMENT`, alterando o status para `PAID` e notificando o cliente.
*   **Entrega**: Marcação de pedidos como entregues para pedidos com status `PAID`, alterando o status para `DELIVERED` e notificando o cliente.

### 4. Cupons de Desconto
*   **Criação e Gestão**: Criar, listar, atualizar e expirar cupons de desconto.
*   **Aplicação**: Aplicar cupons a pedidos, com validação de validade e valor mínimo.

### 5. Regras de Desconto
*   **Criação e Gestão**: Criar, listar, atualizar e desativar regras de desconto simples e compostas.
*   **Flexibilidade**: Estrutura para definir condições e ações de desconto via JSON.

### 6. Segurança
*   **Autenticação JWT**: Autenticação de utilizadores via JSON Web Tokens.
*   **Autorização Baseada em Papéis**: Controle de acesso a endpoints com base nos papéis do utilizador (`ROLE_USER`, `ROLE_ADMIN`).
*   **Validação de Dados**: Verificação de e-mail, validação de CPF (documento de identificação) e prevenção de duplicatas.
*   **Senhas Seguras**: Armazenamento de senhas com `BCryptPasswordEncoder` e requisitos de complexidade (mínimo 6 caracteres, incluindo letras, números e caracteres especiais).
*   **Pagamentos Seguros**: Simulação de processamento de pagamentos (cartão de crédito, PIX, débito) com tokenização de dados sensíveis.

## Tecnologias Utilizadas

### Backend (Java Spring Boot)
*   **Java 21+**
*   **Spring Boot**: Framework principal para construção da API RESTful.
*   **Spring Security**: Autenticação e autorização.
*   **Spring Data JPA**: Persistência de dados com Hibernate.
*   **PostgreSQL**: Banco de dados relacional.
*   **Lombok**: Redução de código boilerplate.
*   **Jakarta Validation**: Validação de dados de entrada.
*   **JavaMail API (simulado)**: Envio de e-mails de notificação.


## Como Executar o Projeto

### Pré-requisitos
*   Java Development Kit (JDK) 21 ou superior
*   Apache Maven
*   Node.js e pnpm (ou npm/yarn)
*   PostgreSQL

### 1. Configuração do Banco de Dados
Crie um banco de dados PostgreSQL (ex: `ecommerce_db`).

### 2. Configuração do Backend
1.  Navegue até o diretório `my-security-app`.
2.  Abra o arquivo `src/main/resources/application.properties` e atualize as credenciais do banco de dados e a chave JWT:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
    spring.datasource.username=postgres
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

    jwt.secret=your_super_secret_key_for_jwt_signing_and_verification_that_is_long_enough
    jwt.expiration=86400000 # 24 hours in milliseconds

    # Email Configuration (Simulated)
    spring.mail.host=smtp.example.com
    spring.mail.port=587
    spring.mail.username=your_email@example.com
    spring.mail.password=your_email_password
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true
    ```
3.  Compile e execute a aplicação Spring Boot:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    A API estará disponível em `http://localhost:8080`.

### 3. Configuração e Execução do Frontend
1.  Navegue até o diretório `my-security-app/frontend`.
2.  Instale as dependências:
    ```bash
    pnpm install
    # ou npm install
    # ou yarn install
    ```
3.  Inicie o servidor de desenvolvimento:
    ```bash
    pnpm run dev
    # ou npm run dev
    # ou yarn dev
    ```
    O frontend estará disponível em `http://localhost:5173` (ou outra porta, conforme indicado pelo Vite).

## Estrutura do Projeto

```
my-security-app/
├── pom.xml
├── src/
│   └── main/
│       └── java/com/example/securityapp/
│           ├── config/                 # Configurações de segurança e outras
│           ├── controller/             # Endpoints da API (REST Controllers)
│           ├── dto/                    # Objetos de Transferência de Dados (DTOs)
│           ├── model/                  # Entidades do banco de dados
│           ├── repository/             # Interfaces de acesso a dados (Spring Data JPA)
│           ├── security/               # Classes relacionadas à segurança (JWT)
│           ├── service/                # Lógica de negócio
│           └── util/                   # Classes utilitárias (validadores)
│       └── resources/
│           └── application.properties  # Configurações da aplicação
└── frontend/
    ├── public/
    ├── src/
    │   ├── assets/
    │   ├── components/                 # Componentes React (Login, Register, ProductManagement, OrderManagement, etc.)
    │   ├── hooks/
    │   ├── lib/
    │   ├── App.jsx                     # Componente principal da aplicação
    │   ├── main.jsx                    # Ponto de entrada do React
    │   └── index.css                   # Estilos globais
    ├── index.html
    ├── package.json
    └── vite.config.js
```

## Endpoints da API 

Para uma lista completa e detalhada dos endpoints, consulte `documentacao_api_ecommerce_integrada.md`.

*   **Registo de Utilizador**: `POST /api/auth/register`
*   **Login de Utilizador**: `POST /api/auth/login`
*   **Listar Produtos**: `GET /api/products`
*   **Criar Pedido**: `POST /api/orders`
*   **Finalizar Pedido**: `POST /api/orders/{orderId}/finalize`
*   **Pagar Pedido**: `POST /api/orders/{orderId}/pay`

## Contribuição

Sinta-se à vontade para clonar o repositório, explorar o código e sugerir melhorias. Para contribuir, por favor, siga as boas práticas de desenvolvimento e crie pull requests.

## Licença

Este projeto está licenciado sob a licença MIT. Consulte o arquivo `LICENSE` para mais detalhes.



