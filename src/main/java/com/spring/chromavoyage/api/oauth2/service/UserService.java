package com.spring.chromavoyage.api.oauth2.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public interface UserService extends OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException;
    String getNaverAuthorizeUrl() throws URISyntaxException, MalformedURLException, UnsupportedEncodingException;
    OAuth2User getAuthenticatedUser();

    String getGoogleAuthorizeUrl()throws URISyntaxException, MalformedURLException, UnsupportedEncodingException;
}
