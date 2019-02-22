CREATE TABLE orders
(
  id                SERIAL                  PRIMARY KEY,
  status            VARCHAR(20)             NOT NULL, -- PAYMENT_WAITED, PAYMENT_CONFIRMED, PAYMENT_REJECTED, CANCELLED,
  created_at        NUMERIC                 NOT NULL,
  updated_at        NUMERIC                 DEFAULT NULL
);
