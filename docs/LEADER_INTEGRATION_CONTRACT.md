# Leader Integration Contract

The sales module is ready to integrate with the Product and People modules through the following minimum contract.

## Product contract

`ProductService` must provide product lookup by identifier, stock validation, stock reduction and persistence after changes. A product used in a sale must expose an identifier and price.

Recommended operations:

- `Product findById(String id)`
- `boolean hasStock(String id, int quantity)`
- `void decreaseStock(String id, int quantity)`

## People contract

`PersonService` must validate customers and sellers by identifier.

Recommended operations:

- `Person findCustomerById(String id)`
- `Person findSellerById(String id)`

## Sales flow

1. Validate customer.
2. Validate seller.
3. Validate that every requested product exists.
4. Validate available stock for every requested quantity.
5. Calculate the total from product prices.
6. Reduce stock only after all validations succeed.
7. Persist inventory changes.
8. Persist the completed sale.

The final integration in `Main` should inject the real Product and People services into the sales service and user interface.
