#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE causea OWNER postgres;
    GRANT ALL PRIVILEGES ON DATABASE causea TO postgres;
EOSQL