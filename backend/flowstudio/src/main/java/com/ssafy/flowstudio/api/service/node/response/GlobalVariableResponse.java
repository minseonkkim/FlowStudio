package com.ssafy.flowstudio.api.service.node.response;

import com.ssafy.flowstudio.domain.chatflow.entity.GlobalVariable;
import com.ssafy.flowstudio.domain.chatflow.entity.GlobalVariableType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GlobalVariableResponse {

    private final Long globalVariableId;
    private final String name;
    private final String value;
    private final GlobalVariableType type;

    @Builder
    private GlobalVariableResponse(Long globalVariableId, String name, String value, GlobalVariableType type) {
        this.globalVariableId = globalVariableId;
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public static GlobalVariableResponse from(GlobalVariable globalVariable) {
        return GlobalVariableResponse.builder()
                .globalVariableId(globalVariable.getId())
                .name(globalVariable.getName())
                .value(globalVariable.getValue())
                .type(globalVariable.getType())
                .build();
    }

}
