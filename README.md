# Service Order Management

- Run `docker-compose up` for **consul** and **postgresql**.
- Run `sh consul_keys.sh` command to add keys and values for Service.
- Connect postgres console `docker exec -it postgres psql -U postgres`
- Create user and database: 
```sh
CREATE USER eriks WITH SUPERUSER CREATEROLE CREATEDB PASSWORD 'eriks';

CREATE DATABASE order_management_service;
```
- Now for initialize tables run sql commands under following file -> ./data/schema.sql
- Run application with this command:
```sh
> gradle bootRun
```
- Run tests with this command:
```sh
> gradle test
```
- Sample requests for test API:

```sh
> export TOKEN=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE1NTA3NzE4ODIsImV4cCI6MTU4MjMwNzg4MiwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSJ9.K8EDMF4-_zzu4l0_gTuEtIpb87nO6wtx_eIk7BYiiXo

> curl -X POST -H "Authorization: Bearer $TOKEN" -H "Content-Type:application/json" http://127.0.0.1:8002/v1/order-management
> curl -X POST -H "Authorization: Bearer $TOKEN" -H "Content-Type:application/json" http://127.0.0.1:8002/v1/order-management
> curl -X POST -H "Authorization: Bearer $TOKEN" -H "Content-Type:application/json" http://127.0.0.1:8002/v1/order-management
> curl -X PUT -H "Authorization: Bearer $TOKEN" -H "Content-Type:application/json" http://127.0.0.1:8002/v1/order-management -d '{"id": 1, "status": "PAYMENT_CONFIRMED"}'
> curl -X PUT -H "Authorization: Bearer $TOKEN" -H "Content-Type:application/json" http://127.0.0.1:8002/v1/order-management -d '{"id": 3, "status": "PAYMENT_REJECTED"}'
> curl -X DELETE -H "Authorization: Bearer $TOKEN" -H "Content-Type:application/json" http://127.0.0.1:8002/v1/order-management/2

```
