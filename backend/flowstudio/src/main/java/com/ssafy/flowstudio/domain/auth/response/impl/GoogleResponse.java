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
	public ProviderType getProvider() {
		return ProviderType.GOOGLE;
	}

	@Override
	public String getProviderId() {
		return attribute.get("sub").toString();
	}

	@Override
	public String getEmail() {
		return attribute.get("email").toString();
	}

}
