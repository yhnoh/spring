package com.example.springbatchstock.stock.daily;

import java.io.Serializable;
import java.time.LocalDate;

public class StockPrimaryKey implements Serializable {
    private String stockCode;
    private LocalDate marketDate;
}
