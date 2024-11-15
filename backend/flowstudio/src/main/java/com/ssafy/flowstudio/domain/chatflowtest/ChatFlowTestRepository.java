package com.ssafy.flowstudio.domain.chatflowtest;

import com.ssafy.flowstudio.domain.chatflowtest.entity.ChatFlowTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatFlowTestRepository extends JpaRepository<ChatFlowTest, Long> {

    @Query("SELECT c FROM ChatFlowTest c JOIN FETCH c.chatFlowTestCases WHERE c.id = :chatFlowTestId")
    Optional<ChatFlowTest> findByIdWithTestCase(Long chatFlowTestId);
}
