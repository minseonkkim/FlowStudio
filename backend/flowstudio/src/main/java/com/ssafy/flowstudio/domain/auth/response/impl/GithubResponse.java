package com.ssafy.flowstudio.domain.auth.response.impl;

import com.ssafy.flowstudio.domain.auth.response.OAuth2Response;
import com.ssafy.flowstudio.domain.user.ProviderType;

import java.util.Map;

public class GithubResponse implements OAuth2Response {
    private final Map<String, Object> attribute;

    public GithubResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getEmail() {
        return attribute.get("login").toString() + "@github.com";
    }

    @Override
    public String getProfileImage() {
        return attribute.get("avatar_url").toString();
    }

    @Override
    public String getNickname() {
        return attribute.get("login").toString();
    }

}
