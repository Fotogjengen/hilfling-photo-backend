FROM node:12
MAINTAINER FG Web (fg-web@samfundet.no)

# Install important dependencies
RUN apk add vim curl

# Create app directory
RUN mkdir /app
WORKDIR /app

# Install app dependencies
# A wildcard is used to ensure both package.json AND package-lock.json are copied
# where available (npm@5+)
#COPY package*.json ./
COPY ./ ./
RUN npm install
# If you are building your code for production
# RUN npm ci --only=production

# Bundle app source

RUN ls -la

#RUN npm i -g nodemon

EXPOSE 8080
CMD ["npm", "run", "start"]
#ENTRYPOINT npm run start