package com.example.springbatchitemreader.database_item_reader.data_repository;

import com.example.springbatchitemreader.database_item_reader.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerEntityJpaRepository extends JpaRepository<CustomerEntity, Long> {

    Page<CustomerEntity> findByCity(String city, Pageable pageable);
}
