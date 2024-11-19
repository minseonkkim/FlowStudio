package com.ssafy.flowstudio.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatEnvVariable {
    INPUT_MESSAGE("input-message"),
    ;
    private final String name;
}
