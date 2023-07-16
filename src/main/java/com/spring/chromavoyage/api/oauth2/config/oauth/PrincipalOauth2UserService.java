package com.spring.chromavoyage.api.oauth2.config.oauth;

import com.spring.chromavoyage.api.oauth2.config.auth.PrincipalDetails;
import com.spring.chromavoyage.api.oauth2.config.oauth.provider.GoogleUserInfo;
import com.spring.chromavoyage.api.oauth2.config.oauth.provider.OAuth2UserInfo;
import com.spring.chromavoyage.api.oauth2.model.User;
import com.spring.chromavoyage.api.oauth2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;

    // userRequest 는 code를 받아서 accessToken을 응답 받은 객체
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필 조회
        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }

        Optional<User> userOptional =
                userRepository.findByProviderAndEmail(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getEmail());

        User user;
        //user가 회원가입이 되어 있는 경우
        if (userOptional.isPresent()) {
            user = userOptional.get();
            // user가 존재하면 update 해주기 => 기능 추가 y or n
			/*user.setUsername(oAuth2UserInfo.getName());
			user.setEmail(oAuth2UserInfo.getEmail());
			user.setPicture(oAuth2UserInfo.getPicture());
			userRepository.save(user);*/
            System.out.println("회원가입 되어 있어 로그인 되었습니다.");
        } else { // user가 회원가입되어 있지 않으므로 자동으로 회원가입
            user = User.builder()
                    .username(oAuth2UserInfo.getName())
                    .email(oAuth2UserInfo.getEmail())
                    .picture(oAuth2UserInfo.getPicture())
                    //.role("ROLE_USER")
                    .provider(oAuth2UserInfo.getProvider())
                    .build();
            userRepository.save(user);
            System.out.println("회원가입 되어 있지 않아 자동으로 회원가입 되었습니다.");
        }
        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
