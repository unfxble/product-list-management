CATALOGUE_SERVICE

docker command for create db

``
docker run --name catalogue-db -p 5432:5432 -e POSTGRES_DB=catalogue -e POSTGRES_USER=catalogue -e POSTGRES_PASSWORD=catalogue postgres:16
``

MANAGER_APP

``
docker run --name manager-db -p 5433:5432 -e POSTGRES_DB=manager -e POSTGRES_USER=manager -e POSTGRES_PASSWORD=manager postgres:16
``