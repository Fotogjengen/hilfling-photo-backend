# Use Traefik v2.10 base image
FROM traefik:v2.10

# Copy the dynamic configuration file to the container
COPY traefik/dynamic.yml /etc/traefik/traefik_dynamic.yml

# Command to run Traefik with the configuration file
CMD ["traefik", "--providers.file.filename=/etc/traefik/traefik_dynamic.yml"]
