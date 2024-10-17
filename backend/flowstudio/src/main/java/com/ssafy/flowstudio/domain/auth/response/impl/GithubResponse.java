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
	public ProviderType getProvider() {
		return ProviderType.GITHUB;
	}

	@Override
	public String getProviderId() {
		return attribute.get("id").toString();
	}

	@Override
	public String getEmail() {
		return attribute.get("email") != null ? attribute.get("email").toString() : "";
	}


}
