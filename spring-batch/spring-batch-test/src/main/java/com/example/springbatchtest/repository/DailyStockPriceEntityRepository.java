package com.example.springbatchtest.repository;

import com.example.springbatchtest.entity.DailyStockPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyStockPriceEntityRepository extends JpaRepository<DailyStockPrice, Long> {


}
