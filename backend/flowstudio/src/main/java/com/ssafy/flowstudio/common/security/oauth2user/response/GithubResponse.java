package com.ssafy.flowstudio.common.security.oauth2user.response;

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
