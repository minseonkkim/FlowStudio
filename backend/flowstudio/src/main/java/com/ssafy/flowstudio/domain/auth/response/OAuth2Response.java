package com.ssafy.flowstudio.domain.auth.response;


import com.ssafy.flowstudio.domain.user.ProviderType;

public interface OAuth2Response {
	ProviderType getProvider();

	String getProviderId();

	String getEmail();

}
