DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL,
    password    VARCHAR(100) NOT NULL,
    real_name   VARCHAR(50),
    role        VARCHAR(20)  NOT NULL,
    phone       VARCHAR(20),
    address     VARCHAR(100),
    status      INT DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS repair_type;
CREATE TABLE repair_type (
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(50) NOT NULL,
    remark VARCHAR(200)
);

DROP TABLE IF EXISTS repair_order;
CREATE TABLE repair_order (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_no       VARCHAR(30) NOT NULL,
    resident_id    BIGINT NOT NULL,
    type_id        BIGINT,
    title          VARCHAR(100) NOT NULL,
    content        VARCHAR(500),
    address        VARCHAR(100),
    urgency        INT DEFAULT 1,
    status         INT DEFAULT 0,
    worker_id      BIGINT,
    handle_remark  VARCHAR(300),
    rating         INT,
    evaluate       VARCHAR(300),
    create_time    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assign_time    TIMESTAMP,
    finish_time    TIMESTAMP
);

DROP TABLE IF EXISTS order_log;
CREATE TABLE order_log (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id      BIGINT NOT NULL,
    operator_id   BIGINT,
    operator_name VARCHAR(50),
    action        VARCHAR(30),
    remark        VARCHAR(300),
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS notice;
CREATE TABLE notice (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    content     VARCHAR(500),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE sys_user ALTER COLUMN id RESTART WITH 100;
ALTER TABLE repair_type ALTER COLUMN id RESTART WITH 100;
ALTER TABLE repair_order ALTER COLUMN id RESTART WITH 200;
ALTER TABLE order_log ALTER COLUMN id RESTART WITH 300;
ALTER TABLE notice ALTER COLUMN id RESTART WITH 100;
