# Imagem oficial do Cypress com todas as dependências do SO (navegadores) incluídas
FROM cypress/included:12.17.4

WORKDIR /app

# Copia os arquivos de dependência primeiro para aproveitar o cache
COPY package*.json ./
RUN npm install

# Copia o restante dos testes
COPY . .

# O comando padrão será rodar o cypress no modo headless
ENTRYPOINT ["npx", "cypress", "run"]