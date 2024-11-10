package com.ssafy.flowstudio.domain.node.repository;

import com.ssafy.flowstudio.domain.node.entity.Retriever;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RetrieverRepository extends JpaRepository<Retriever, Long> {
    @Query("select r from Retriever r right join Node n on r.id = n.id where n.id = :id")
    Optional<Retriever> findById(Long id);
}
