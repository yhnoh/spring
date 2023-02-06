package com.example.springbatchstock;

import java.io.Serializable;
import java.time.LocalDate;

public class StockPrimaryKey implements Serializable {
    private String stockCode;
    private LocalDate marketDate;
}
