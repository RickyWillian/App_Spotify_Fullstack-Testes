# Jukebox Spotify - Full Stack & Test Automation

![Cypress](https://img.shields.io/badge/-cypress-%2304C38E?style=for-the-badge&logo=cypress&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%232496ED.svg?style=for-the-badge&logo=docker&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)

Este projeto é uma aplicação **Full Stack** integrada à API do Spotify, desenvolvida para demonstrar habilidades em **Desenvolvimento de Software** e, principalmente, **Automação de Testes (QA)**. O ecossistema é totalmente containerizado, permitindo que a aplicação e sua suíte de testes rodem em qualquer ambiente com um único comando.

---

## Diferenciais do Projeto (QA Focus)

Diferente de uma aplicação comum, este repositório foi construído com foco em **Continuous Testing**:

* **Orquestração com Docker Compose:** Ambiente isolado para Backend, Frontend e Testes.
* **Estratégia de Mocking:** Utilização do `cy.intercept` no Cypress para simular respostas da API do Spotify, garantindo que o pipeline de testes seja resiliente a falhas externas ou limitações de conta (como o erro 403 Forbidden).
* **Healthcheck Inteligente:** O container de testes aguarda o status "Healthy" do Backend para iniciar a execução, simulando uma esteira de CI/CD real.
* **Reports em Terminal:** Configuração para encerrar containers automaticamente após a execução e retornar o status code dos testes.

---

## Tecnologias

### **Backend**
* **Java 17** com **Spring Boot**
* **Spring Actuator** (Monitoramento e Healthcheck)
* **Spotify Web API** (Integração e OAuth2)

### **Frontend**
* **React.js** (Hooks e Axios para consumo de API)

### **DevOps & QA**
* **Docker & Docker Compose**
* **Cypress** (E2E, Sanity e Mocked Tests)

---

## Como Rodar o Projeto

O projeto utiliza variáveis de ambiente para segurança. Siga os passos abaixo:

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/SEU_USUARIO/AppSpotify.git](https://github.com/SEU_USUARIO/AppSpotify.git)
    cd AppSpotify
    ```

2.  **Configure as credenciais:**
    Crie um arquivo `.env` na raiz do projeto com suas chaves do [Spotify Developer](https://developer.spotify.com/):
    ```env
    SPOTIFY_CLIENT_ID=seu_id
    SPOTIFY_CLIENT_SECRET=seu_secret
    SPOTIFY_REFRESH_TOKEN=seu_token
    ```

3.  **Suba o ambiente e execute os testes:**
    O comando abaixo constrói as imagens e dispara a suíte de testes automaticamente:
    ```bash
    docker compose up --build --exit-code-from cypress
    ```

---

## Estrutura de Testes

Os testes estão localizados em `/jukebox-test/cypress/e2e/` e cobrem:

* **Interface Sanity:** Garante que os elementos visuais críticos estão acessíveis.
* **Mocked Search:** Valida o fluxo de busca e exibição de cards simulando a resposta da API (ideal para CI/CD).
* **E2E Integration:** Teste de ponta a ponta com a API real do Spotify.

---

## Contexto
Projeto desenvolvido por **Ricky**, estudante de **Sistemas de Informação** na **Universidade Federal de Uberlândia (UFU)**, com foco em Engenharia de Software e Qualidade.