# jUnit Mockito Sample

A tiny unit testing sample with a few mocks to test a service class.

## Structure
- `Order`  is a plain model
- `OrderRepository` the abstraction over the DB layer
- `OrderService`  is what is tested.
- `PaymentService`  a stub service class that gets involved during completing an order

## Test notes
- Mocks are for all external sources not part of the unit being tested
- Should assert the desired results
- Should verify that the stubs are interacted with (to represent dealing with external dependencies)