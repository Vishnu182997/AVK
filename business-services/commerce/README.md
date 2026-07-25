# Smart Commerce

A Spring modular monolith for authentication, catalogue, relational carts and wishlists, transactional checkout, coupons, mock payments, reviews, notifications, auditing, and sales reporting.

## Run

1. Export a Base64-encoded JWT signing secret of at least 32 bytes: `export JWT_SECRET=$(openssl rand -base64 48)`.
2. Run `mvn -pl business-services/commerce -am package`.
3. Run `docker compose -f business-services/commerce/docker-compose.yml up --build`.

OpenAPI is at `/swagger-ui.html`; health and metrics are under `/actuator`. Public registration always creates a `CUSTOMER`. Seller and admin assignment is an operational administrator action, never a public API input.

## Consistency

Checkout is a single database transaction. Inventory rows are pessimistically locked in product-id order before current prices are snapshotted, stock is deducted, coupon usage is recorded, and the cart is cleared. This prevents overselling and lock-order deadlocks. Notifications consume Spring events with `AFTER_COMMIT`, so rolled-back work cannot notify users.

Payments use a client idempotency key protected by a unique database constraint. Repeating the same key and order/amount fingerprint returns the existing payment; using the key for a different logical request returns `409 Conflict`. The local mock provider stores no card data. Its webhook is admin-protected.

Redis and a message broker are intentionally omitted because no established repository infrastructure exists. Replace the relational cart or transaction-aware event listener behind the service boundary if those facilities are introduced later.
