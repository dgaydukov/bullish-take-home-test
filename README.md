# Electronic Store

### Run App
* run app with `mvn clean spring-boot:run` (run spring app on port `8080`)
* run tests only `mvn clean test`

### Features
* universal exception handling with spring aspects
* unique exception for each exception cases (help quickly identify the issue without going deep into exception message)
* AOP logging - we have aspect to log each controller method execution time (for example we can track with devops tools any call that take longer then 500ms)

### Tests
There are 2 types of tests here:
* unit tests - where we test code of each class separately, using mocks if our class depends upon other classes (you mostly use mocks here to imitate dependency access).
* integration tests - were we basically launch whole application, load spring context and test it as it is fully running (no mocks here only real objects, cause in background whole app is running).
There are 3 main components in this app:
* controllers - tested as part of integration tests
* services - tested with unit test for each service method
* models & repos - not tested at all, cause there is nothing to test there except getter/setter which automatically generated by lombok

### Local development
By default we are using in-memory db, but you can enable persistence into file, so you can do local development with persistent db
* add this to props file to enable file persistence `spring.datasource.url=jdbc:h2:file:/data/demo`
* add this to property file to enable h2 console to modify db from web-based console `spring.h2.console.enabled=true`
Run locally:
```
# add products
curl -H 'Content-Type: application/json' -d '{"name":"spoon","weight":100,"quantity":10}' http://localhost:8080/admin/product
curl -H 'Content-Type: application/json' -d '{"name":"fork","weight":100,"quantity":10}' http://localhost:8080/admin/product
# add prices
curl -H 'Content-Type: application/json' -d '{"price":50}' http://localhost:8080/admin/product/1/price
curl -H 'Content-Type: application/json' -d '{"price":100}' http://localhost:8080/admin/product/2/price
# add deal
curl -H 'Content-Type: application/json' -d '{"name":"Get 50% off for second item","dealClassName":"Get50PercentOffForSecondItem"}' http://localhost:8080/admin/deal
# list procuts
curl -H 'Content-Type: application/json' http://localhost:8080/admin/product
# add product to cart
curl -H 'Content-Type: application/json' -d '{"userId":10,"productId":1,"quantity":5}' http://localhost:8080/user/cart/product
curl -H 'Content-Type: application/json' -d '{"userId":10,"productId":2,"quantity":5}' http://localhost:8080/user/cart/product
# calculate receipt => {"userId":10,"totalPrice":600.0}
curl -H 'Content-Type: application/json' -d '{"userId":10}' http://localhost:8080/user/cart/receipt
```

### TODO
* Add spring boot actuator with health info (would be used by CI/CD pipeline)
* Add simple authenticate for admin/user controllers with spring security
* Add cucumber integration tests
* Add order table where to store actual user orders
* Add CI/CD pipeline with deployment to aws