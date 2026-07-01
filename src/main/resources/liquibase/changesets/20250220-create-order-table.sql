--liquibase formatted sql

--changeset author:1
CREATE TABLE IF NOT EXISTS orders
(
    id           UUID PRIMARY KEY,
    customer_id  UUID                     NOT NULL,
    region       VARCHAR(50)              NOT NULL,
    priority     VARCHAR(10)              NOT NULL,
    amount       DECIMAL(19, 2)           NOT NULL,
    status       VARCHAR(20)              NOT NULL,
    event_id     VARCHAR(100) UNIQUE      NOT NULL,
    kafka_offset BIGINT,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITH TIME ZONE,
    processed_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_orders_status ON orders (status);
CREATE INDEX idx_orders_created_at ON orders (created_at);
CREATE INDEX idx_orders_event_id ON orders (event_id);

--rollback DROP TABLE orders;

--changeset author:2
CREATE TABLE IF NOT EXISTS order_lines
(
    id         UUID PRIMARY KEY,
    order_id   UUID           NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    product_id UUID           NOT NULL,
    quantity   INTEGER        NOT NULL,
    price      DECIMAL(19, 2) NOT NULL,
    priority   VARCHAR(10)
);

CREATE INDEX idx_order_lines_order_id ON order_lines (order_id);
CREATE INDEX idx_order_lines_product_id ON order_lines (product_id);

--rollback DROP TABLE order_lines;
