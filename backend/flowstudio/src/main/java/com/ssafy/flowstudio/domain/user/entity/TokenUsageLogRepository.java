package com.ssafy.flowstudio.domain.user.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenUsageLogRepository extends JpaRepository<TokenUsageLog, Long> {
}
