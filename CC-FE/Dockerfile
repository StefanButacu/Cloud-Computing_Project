FROM node:19.5.0-alpine
# copy package files
COPY package.json ./
COPY package-lock.json ./
# install dependencies
RUN npm install
COPY ./ ./
# ionic serve
EXPOSE 3000
CMD ["npm", "start"]