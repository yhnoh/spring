package com.example.associatedrelationship.mapping.repository;

import com.example.associatedrelationship.mapping.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoodsJpaRepository extends JpaRepository<Goods, Long> {

    List<Goods> findByNameIn(List<String> name);
}
