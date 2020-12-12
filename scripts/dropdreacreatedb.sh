docker-compose exec postgres psql -U hilfling -d postgres --command "drop database hilflingdb"
docker-compose exec postgres psql -U hilfling -d postgres --command "create database hilflingdb"
