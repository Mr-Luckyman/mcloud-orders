-- liquidbase fomatted sql

CREATE TABLE IF NOT EXISTS orders
(
    id          UUID PRIMARY KEY,
    customer_id UUID           NOT NULL,
    amount      DECIMAL(19, 2) NOT NUll,
    status      VARCHAR(20)    NOT NULL,
    created_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP
);

CREATE INDEX idx_orders_status ON orders (status);
CREATE INDEX idx_order_created_on on orders (created_at);

--rollback DROP table order;
