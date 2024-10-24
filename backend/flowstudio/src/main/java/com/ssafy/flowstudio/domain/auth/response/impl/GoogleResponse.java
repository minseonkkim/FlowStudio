package com.ssafy.flowstudio.domain.auth.response.impl;


import com.ssafy.flowstudio.domain.auth.response.OAuth2Response;
import com.ssafy.flowstudio.domain.user.ProviderType;

import java.util.Map;

public class GoogleResponse implements OAuth2Response {
	private final Map<String, Object> attribute;

	public GoogleResponse(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	@Override
	public String getEmail() {
		return attribute.get("email").toString();
	}

	@Override
	public String getProfileImage() {
		return attribute.get("picture") != null ? attribute.get("picture").toString() : "";
	}

	@Override
	public String getNickname() {
		return attribute.get("name").toString();
	}

}
