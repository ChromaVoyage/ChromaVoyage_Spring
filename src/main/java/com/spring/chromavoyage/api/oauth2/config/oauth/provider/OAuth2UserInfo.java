package com.spring.chromavoyage.api.oauth2.config.oauth.provider;

public interface OAuth2UserInfo {
    String getProvider();
    String getEmail();
    String getName();
    String getPicture();
}
