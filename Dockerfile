FROM node:12-alpine

# Install important dependencies
RUN apk add vim curl

# Create app directory
RUN mkdir /app
WORKDIR /app

# Install app dependencies
# A wildcard is used to ensure both package.json AND package-lock.json are copied
# where available (npm@5+)
COPY package*.json ./

RUN npm install
# If you are building your code for production
# RUN npm ci --only=production

# Bundle app source
COPY . ./
RUN ls -la

EXPOSE 8080
CMD [ "nodemon --watch src --ext ts --exec ts-node --ignore '*.test.ts' --delay 3 src/server.ts" ]
#ENTRYPOINT npm run start