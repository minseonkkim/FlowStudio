package com.ssafy.flowstudio.domain.knowledge.entity;

import feign.Param;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge, Long> {

    List<Knowledge> findByUserId(Long userId, PageRequest pageable);

    Optional<Knowledge> findByUserIdAndId(Long userId, Long id);

    @Query("select k from Knowledge k where k.id = :id and k.isPublic = :isPublic")
    Optional<Knowledge> findByIdAndPublic(Long id, Boolean isPublic);

    @Query(
            "select k from Knowledge k"
            + " join fetch Retriever r"
            + " on r.knowledge.id = k.id"
            + " where r.knowledge.id = k.id"
            + " and r.chatFlow.id = :chatFlowId"
    )
    List<Knowledge> findByChatFlowId(Long chatFlowId);
}
