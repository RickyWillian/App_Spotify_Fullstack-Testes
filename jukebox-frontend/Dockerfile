# Usa uma imagem do Node.js
FROM node:18-alpine
WORKDIR /app
# Copia os arquivos de dependências
COPY package*.json ./
RUN npm install
# Copia o restante do código
COPY . .
# Expõe a porta padrão do React
EXPOSE 3000
# Comando para iniciar o front
CMD ["npm", "start"]