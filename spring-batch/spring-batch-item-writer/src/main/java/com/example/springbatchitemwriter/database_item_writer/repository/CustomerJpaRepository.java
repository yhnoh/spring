package com.example.springbatchitemwriter.database_item_writer.repository;

import com.example.springbatchitemwriter.database_item_writer.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, Long> {

}
