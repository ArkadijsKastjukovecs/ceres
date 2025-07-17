CREATE TABLE visits
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    tracking_prefix CHAR(3)  NOT NULL,
    tracking_number INT      NOT NULL,
    visit_date      DATETIME NOT NULL,
    product         VARCHAR(255),

    INDEX           idx_tracking (tracking_prefix, tracking_number),
    INDEX           idx_visit_date (visit_date)
);

CREATE TABLE sales
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    visit_id          INT NOT NULL,
    sale_date         DATETIME,
    sale_price        DECIMAL(10, 2),
    commission_amount DECIMAL(10, 2),

    CONSTRAINT fk_sales_visit FOREIGN KEY (visit_id)
        REFERENCES visits (id)
        ON DELETE CASCADE,

    INDEX             idx_sale_date (sale_date)
);