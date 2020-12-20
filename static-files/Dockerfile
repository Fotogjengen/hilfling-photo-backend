# production environment
FROM nginx:1.19.6-alpine
COPY ./static/ /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

