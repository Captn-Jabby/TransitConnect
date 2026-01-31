package com.connect.transitconnect.repository;

import com.connect.transitconnect.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<RouteEntity, Long> {
}
