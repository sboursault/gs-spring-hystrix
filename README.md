# spring-hystrix-correlation-id

Correlation id implementation with Spring and Hystrix

## What is a correlation id ?

When working with micro-services, tracing the root cause of an issue can quickly become a headache. Requests are passed from a system to another, and a simple stack trace may not bear the information you need.

The correlation id is a simple diagnostic tool. Basically, it's a unique identifier for a business transaction. This identifier is shared across several systems so that each system can print logs with this correlation id.

This way, it's possible to aggregate all the logs related to a single business transaction.

This practice is sometimes refered as "distributed tracing"

## How does it work ?

A simple solution is to pass the correlation id from one system to another as a http header.

## How does it work with Hystrix ?
 
ThreadLocal won't do
the correlation-id is a cross-cuting concern. We don't want to add noise to our business code.

resources :
    http://samnewman.io/talks/principles-of-microservices/
    https://dzone.com/articles/implementing-correlation-ids-0