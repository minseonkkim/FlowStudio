package com.ssafy.flowstudio.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String paramList;

    @Builder
    private Model(Long id, String name, String paramList) {
        this.id = id;
        this.name = name;
        this.paramList = paramList;
    }

    public static Model create(String name, String paramList) {
        return Model.builder()
                .name(name)
                .paramList(paramList)
                .build();
    }

}
