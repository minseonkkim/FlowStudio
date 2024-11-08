package com.ssafy.flowstudio.domain.knowledge.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge, Long> {

    List<Knowledge> findByUserId(Long userId);
    Optional<Knowledge> findByUserIdAndId(Long userId, Long id);
}
