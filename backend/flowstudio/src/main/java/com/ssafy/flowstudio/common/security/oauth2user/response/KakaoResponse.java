package com.ssafy.flowstudio.common.security.oauth2user.response;


import java.util.Map;

public class KakaoResponse implements OAuth2Response {
	private final Map<String, Object> attribute;
	private final Map<String, Object> properties;
	private final Map<String, Object> kakaoAccount;

	public KakaoResponse(Map<String, Object> attributes) {
		this.attribute = attributes;
		this.properties = (Map<String, Object>)attributes.get("properties");
		this.kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
	}

	@Override
	public String getEmail() {
		return kakaoAccount.get("email").toString();
	}

	@Override
	public String getProfileImage() {
		return properties.get("profile_image") != null ? properties.get("profile_image").toString() : "";
	}

	@Override
	public String getNickname() {
		return properties.get("nickname").toString();
	}

}
