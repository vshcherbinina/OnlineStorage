# Online-store project

This project is a microservice architecture consisting of 7 modules written in Java 17.0 based on the Spring Boot group frameworks
and demonstrates the implementation of the choreographic saga template.

The services include 3 technical ones:

1. Gateway-service
2. Discovery-service (combined with Config-service)
3. Auth-service

The following services are responsible for servicing business logic:
1. Order-service: formation of an order and transfer it to Payment-service for payment. Updating order status and storing state history by processing messages from other services about the results of order processing.
2. Payment-service: maintaining cards of clients and their accounts, incl. replenishment of balance. Payment for an incoming order and informing the Inventory-service about the need to complete the order.
3. Inventory-service: creation and storage of product cards, including the receipt of products. Processes orders and completes packaging. If the configuration is successful, it sends a message to the Delivery-service.
4. Delivery-service: is sketchy service. Sends a successful or unsuccessful delivery message to the Order-service. Successful delivery is achieved randomly in 75% of orders.

Thus, an order for the purchase of certain products passes through the payment, packaging and delivery service. 
Implemented a distributed transaction using the choreographic saga pattern. If a problem occurs in one of the payment or packaging services, compensatory actions are performed - for the payment service, if the order is not completed, a transaction is created to return funds to the client’s account. The order service updates the status.


The process is presented in more detail in the diagram below:

![Online-store infrastructure2.png](png%2FOnline-store%20infrastructure2.png)

Online-store choreographic saga pattern with kafka:

![Online-store saga kafka.png](png%2FOnline-store%20saga%20kafka.png)

Each service stores information in its own PostrgeSQL database. Detailed diagram:

![Online-store postres storage data.png](png%2FOnline-store%20postres%20storage%20data.png)


## Environment

To run PostgreSQL with Kafka you have to execute the command in project root Report-service:
```
$sudo docker-compose up -d
```

Also, you have convenient [UI for Apache Kafka](https://github.com/provectus/kafka-ui) at URL

http://localhost:9999/

Now you can run application services from your IDE in this order 
- Discovery
- Auth-service
- Order-service, Payment-service, Inventory-service, Delivery-service
- Gateway-service

After that you can find Swagger UI of every service at URL.

http://localhost:8080/api/swagger-ui/index.html



At Gateway, you can find joined Swagger UI
http://localhost:9090/swagger-ui.html

## Basic interactions

You can use those curl commands, or you can do all that with Swagger UI

### Authentication

To create user use this request to auth service 
```bash
curl -X 'POST' \
  'http://localhost:9090/auth/user/signup' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "first",
  "password": "password"
}'
```

After that you can get a token
```bash
curl -X 'POST' \
  'http://localhost:9090/auth/auth/token/generate' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "first",
  "password": "password"
}'
```

Now you can use this token to authenticate requests to other services.


### Payment service

First, you should create a client with a positive balance of payments:

```bash
curl -X 'POST' \
  'http://localhost:9090/api/client' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H 'Bearer <put token here>'
  -d '{
    "userName": "first",
    "name": "Client First",
    "balance": 1000
}'
```

### Inventory service

The next important step necessary for successful order completion is product creation:

```bash
curl -X 'POST' \
  'http://localhost:9090/api/product' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H 'Bearer <put token here>'
  -d '{
    "article": "Cream",
    "price": 50,
    "quantity": 5,
    "unit": "pc",
    "description": "test",
    "stockAddress": "Moscow, Lenina st., 1"
} 
'
```

### Order service

Now create an order:

```bash
curl -X 'POST' \
  'http://localhost:9090/api/order' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H 'Bearer <put token here>'
  -d '{
    "userName": "first",
    "amount": 50.0,
    "destinationAddress": "Saint Petersburg, Nevsky Avenue, 1",
    "description": "test1",
    "details": [
        {
            "productArticle": "Cream",
            "price": 50.0,
            "quantity": 1.0,
            "amount": 50.0
        }
    ]
}'
```

That's all.
You can verify this by making a request to the order service and checking the order status. 
It must be DELIVERED or DELIVERY_FAILED:

```bash
curl -X 'GET' \
  'http://localhost:9090/api/order' \
  -H 'accept: */*' \
  -H 'Bearer <put token here>'
```


Or, by order id:

```bash
curl -X 'GET' \
  'http://localhost:9090/api/order/1' \
  -H 'accept: */*' \
  -H 'Bearer <put token here>'
```


In a similar way you can look at:

List of all transactions: 'http://localhost:9090/api/transaction'

List of all payments: 'http://localhost:9090/api/payment'

List of all clients: 'http://localhost:9090/api/client'

List of all products: 'http://localhost:9090/api/product'



To replenish the client's balance, use:

```bash
curl -X 'POST' \
  'http://localhost:9090/api/client/balance' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
    "userName": "first",
    "amount": 100.00
}'
```

To increase the quantity of a product, use the same POST request as to create a product (just enter the article and quantity)




## Running all services with docker-compose

To run all services with docker-compose use this command
```bash
 docker-compose -f docker-compose.yml -f docker-compose.services.yml up -d
```
#### Note
Also, to run all services with docker, you need to change the kafka.bootstrap-servers setting - for services using kafka (order, payment, inventory, delivery), in the discovery configuration files