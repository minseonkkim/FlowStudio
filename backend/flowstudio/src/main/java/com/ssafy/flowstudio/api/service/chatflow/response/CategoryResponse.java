package com.ssafy.flowstudio.api.service.chatflow.response;

import com.ssafy.flowstudio.domain.chatflow.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryResponse {

    private final Long categoryId;
    private final String name;

    @Builder
    private CategoryResponse(Long categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }

}
