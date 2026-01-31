package com.connect.transitconnect.repository;

import com.connect.transitconnect.entity.StopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StopRepository extends JpaRepository<StopEntity, Long> {
}
