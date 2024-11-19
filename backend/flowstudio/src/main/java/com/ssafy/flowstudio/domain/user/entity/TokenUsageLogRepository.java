package com.ssafy.flowstudio.domain.user.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TokenUsageLogRepository extends JpaRepository<TokenUsageLog, Long> {
    @Query("SELECT t FROM TokenUsageLog t WHERE t.user.id = :userId AND t.createdAt >= :startDate ORDER BY t.createdAt DESC")
    List<TokenUsageLog> findTokenUsageLogs(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);
}
