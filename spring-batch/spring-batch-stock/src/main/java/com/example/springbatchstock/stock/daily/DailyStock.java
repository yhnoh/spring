package com.example.springbatchstock.stock.daily;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@IdClass(StockPrimaryKey.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "daily_stock")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DailyStock {
    @Id
    private String stockCode;
    @Id
    private LocalDate marketDate;
    private Double openPrice;
    private Double highPrice;
    private Double lowPrice;
    private Double closePrice;
    private Long volume;

}
