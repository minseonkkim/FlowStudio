package com.ssafy.flowstudio.domain.chatflow.repository;

import com.ssafy.flowstudio.domain.chatflow.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
