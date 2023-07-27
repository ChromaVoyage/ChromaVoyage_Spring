package com.spring.chromavoyage.api.oauth2.config.oauth;

import com.spring.chromavoyage.api.oauth2.config.auth.OAuthAttributes;
import com.spring.chromavoyage.api.oauth2.config.auth.PrincipalDetails;
import com.spring.chromavoyage.api.oauth2.config.oauth.provider.GoogleUserInfo;
import com.spring.chromavoyage.api.oauth2.config.oauth.provider.OAuth2UserInfo;
import com.spring.chromavoyage.api.oauth2.model.User;
import com.spring.chromavoyage.api.oauth2.model.UserDto;
import com.spring.chromavoyage.api.oauth2.repository.UserRepository;
import com.spring.chromavoyage.api.oauth2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService implements UserService   {
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    //@Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private final String REDIRECT_URI = "http://localhost:8080/login/oauth2/code/naver";
    //@Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private final String CLIENT_ID = "_2g1H0TJrHEqeYmWePhR";
    //@Value("${spring.security.oauth2.client.provider.naver.authorization-uri}")
    private final String AUTHORIZATION_URI = "https://nid.naver.com/oauth2.0/authorize";

    // userRequest 는 code를 받아서 accessToken을 응답 받은 객체
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필 조회
        System.out.println(userRequest.getAccessToken().getTokenValue());
        return processOAuth2User(userRequest, oAuth2User);
    }
    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = null;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if (registrationId.equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());

            Optional<User> userOptional =
                    userRepository.findByProviderAndEmail(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getEmail());
            User user;
            //user가 회원가입이 되어 있는 경우
            if (userOptional.isPresent()) {
                user = userOptional.get();
                System.out.println("로그인 되었습니다.");
            } else { // user가 회원가입되어 있지 않으므로 자동으로 회원가입
                user = User.builder()
                        .username(oAuth2UserInfo.getName())
                        .email(oAuth2UserInfo.getEmail())
                        .picture(oAuth2UserInfo.getPicture())
                        .provider(oAuth2UserInfo.getProvider())
                        .build();
                userRepository.save(user);
                System.out.println("회원가입 되어 있지 않아 자동으로 회원가입 되었습니다.");
            }
            return new PrincipalDetails(user, oAuth2User.getAttributes());

        }else if(registrationId.equals("naver")){
            String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                    .getUserInfoEndpoint().getUserNameAttributeName();
            OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

            User user = saveOrUpdate(attributes);
            System.out.println(user.getUsername());

        }
        throw new RuntimeException("no match registrationId");
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        final String naverProvider = "NAVER";
        Optional<User> optionalUser = userRepository.findByEmail(attributes.getEmail());
        if(optionalUser.isPresent()){
            User user  = optionalUser.get();
            user.updateNmAndPicture(attributes.getName(), attributes.getPicture());
            return user;
        }else {
            User user = attributes.toEntity();
            user.updateProvider(naverProvider);
            return userRepository.save(user);
        }
    }

    public String getNaverAuthorizeUrl() throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {
        String authorizationUri = AUTHORIZATION_URI;
        String clientId = CLIENT_ID;
        String redirectUrl = REDIRECT_URI;

        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(authorizationUri)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", URLEncoder.encode(redirectUrl, "UTF-8"))
                .queryParam("state", URLEncoder.encode("1234", "UTF-8"))
                .build();

        return uriComponents.toString();
    }
    // OAuth2 인증 정보를 가져오는 메서드
    public OAuth2User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            return (OAuth2User) authentication.getPrincipal();
        }
        return null;
    }
}
