CREATE TABLE EXECUTION_TYPE(
    id          uuid,
    name        VARCHAR NOT NULL,
    PRIMARY KEY (id),
    UNIQUE(name)
);

INSERT INTO EXECUTION_TYPE VALUES(
    '0ce2585e-8d35-4700-bd85-1ec08ed8815b',
    'MOMENTUM'
);

CREATE TABLE EXECUTION(
    id                  uuid,
    startdate           date,
    enddate             date,
    EXECUTION_TYPE_ID   uuid,
    PRIMARY KEY (id),
    FOREIGN KEY (EXECUTION_TYPE_ID) REFERENCES EXECUTION_TYPE(id)
);

CREATE TABLE STOCK_MARKET(
    id                      uuid,
    name                    varchar(36),
    PRIMARY KEY (id)
);

CREATE TABLE STOCK(
    id                      uuid,
    name                    varchar(36),
    ticker                  varchar(36),
    stock_market_id         uuid,
    PRIMARY KEY (id),
    FOREIGN KEY (STOCK_MARKET_ID) REFERENCES STOCK_MARKET(id)
);

CREATE TABLE EXECUTION_RESULT(
    id                      uuid,
    execution_result_name   varchar(36),
    execution_result_value  decimal,
    stock_id                uuid,
    execution_id            uuid,
    PRIMARY KEY (id),
    FOREIGN KEY (execution_id)           REFERENCES EXECUTION(id),
    FOREIGN KEY (stock_id)               REFERENCES STOCK(id)
);

