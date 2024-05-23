# Use Traefik v2.10 base image
FROM traefik:v2.10

# Copy the dynamic configuration file to the container
COPY traefik/dynamic.yml /etc/traefik/traefik_dynamic.yml
# Copy the static configuration file to the container
COPY traefik/static.yml /etc/traefik/traefik_static.yml

# Command to run Traefik with the configuration file
CMD ["traefik", "--configfile=/etc/traefik/traefik_static.yml", "--providers.file.filename=/etc/traefik/traefik_dynamic.yml"]
