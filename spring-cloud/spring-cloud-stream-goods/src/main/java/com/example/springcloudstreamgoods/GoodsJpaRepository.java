package com.example.springcloudstreamgoods;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsJpaRepository extends JpaRepository<GoodsEntity, Long> {

}
