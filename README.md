# Controle Financeiro

## 📌 Sobre o projeto
O **Controle Financeiro** é uma aplicação que permite gerenciar receitas e despesas, oferecendo um balanço financeiro detalhado por categoria.

## 🚀 Tecnologias Utilizadas
- **Java** + **Spring Boot**
- **H2 Database**
- **JUnit** + **Mockito** (Testes Unitários)
- **Swagger** (Documentação da API)

## 🔧 Como rodar o projeto

### 1️⃣ Clonar o repositório
```bash
git clone https://github.com/priscyladepaula/controle-financeiro.git
cd controle-financeiro
```

### 2️⃣ Configurar e rodar a aplicação
Para rodar localmente:
```bash
./mvnw spring-boot:run
```
A API ficará disponível em: `http://localhost:8080`

## 📖 Documentação da API
Os endpoints estão documentados no Swagger:
```
http://localhost:8080/swagger-ui.html
```

### 🔑 Configuração da API Key no Swagger
Para autenticação nos endpoints protegidos, é necessário configurar a API Key no Swagger:
1. Acesse o Swagger UI (`http://localhost:8080/swagger-ui.html`).
2. Clique no botão **Authorize**.
3. Insira a API Key no formato esperado.
4. Confirme clicando em **Authorize**.
5. Agora os endpoints protegidos podem ser acessados normalmente.

## 📂 Estrutura do Projeto
```
controle-financeiro/
├── src/main/java/br/com/priscyladepaula/desafioitau
│   ├── controller/       # Controllers da API
│   ├── service/          # Regras de negócio
│   ├── infrastructure/   # Acesso a dados
│   ├── domain/           # Entidades
│   ├── dto/              # Data Transfer Objects
│   ├── exception/        # Tratamento de erros
│   ├── security/         # Autenticação da api-key
├── src/main/java/br/com/priscyladepaula/desafioitau  # Testes unitários
├── README.md             # Documentação
```

## 🤝 Contribuição
1. **Fork** o repositório
2. Crie uma branch (`git checkout -b minha-feature`)
3. Commit suas alterações (`git commit -m 'Adicionando nova funcionalidade'`)
4. Suba para o GitHub (`git push origin minha-feature`)
5. Abra um Pull Request 🚀

---

📌 **Desenvolvido por [Priscyla de Paula](https://github.com/priscyladepaula/)**

