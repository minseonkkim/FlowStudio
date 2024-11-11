package com.ssafy.flowstudio.domain.user.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TokenUsageLogRepository extends JpaRepository<TokenUsageLog, Long> {
    @Query("SELECT t FROM TokenUsageLog t WHERE t.user = :user")
    List<TokenUsageLog> findByUser(User user);
}
