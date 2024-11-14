package com.ssafy.flowstudio.domain.chatflowtest;

import com.ssafy.flowstudio.domain.chatflowtest.entity.ChatFlowTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatFlowTestRepository extends JpaRepository<ChatFlowTest, Long> {
}
