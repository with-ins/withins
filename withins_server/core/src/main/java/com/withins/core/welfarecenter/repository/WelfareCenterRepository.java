package com.withins.core.welfarecenter.repository;

import com.withins.core.welfarecenter.entity.WelfareCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for WelfareCenter entities.
 */
@Repository
public interface WelfareCenterRepository extends JpaRepository<WelfareCenter, Long> {
    Optional<WelfareCenter> findByName(String name);
}