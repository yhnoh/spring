create table stock_price(
    stock_code varchar(8) not null,
    market_date date not null,
    open_price decimal(18, 2),
    high_price decimal(18, 2),
    low_price decimal(18, 2),
    close_price decimal(18, 2),
    volume long,
    PRIMARY KEY (stock_code, market_date)
);