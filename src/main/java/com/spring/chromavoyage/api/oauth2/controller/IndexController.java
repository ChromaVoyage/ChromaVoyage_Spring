package com.spring.chromavoyage.api.oauth2.controller;

import com.spring.chromavoyage.api.oauth2.config.auth.PrincipalDetails;
import com.spring.chromavoyage.api.oauth2.config.oauth.PrincipalOauth2UserService;
import com.spring.chromavoyage.api.oauth2.model.User;
import com.spring.chromavoyage.api.oauth2.model.UserDto;
import com.spring.chromavoyage.api.oauth2.repository.UserRepository;
import com.spring.chromavoyage.api.oauth2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Iterator;

@Controller
@RequiredArgsConstructor
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    private final PrincipalOauth2UserService principalOauth2UserService;
//
//    @Autowired
//    public IndexController(PrincipalOauth2UserService principalOauth2UserService) {
//        this.principalOauth2UserService = principalOauth2UserService;
//    }

    @GetMapping({ "", "/" })
    public @ResponseBody String main() {
        return "메인 페이지입니다.";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principal) {
        //System.out.println("Principal : " + principal);
        System.out.println("OAuth2 : "+principal.getUser().getProvider());
        System.out.println("User-name : "+principal.getUser().getUsername());
        System.out.println("User-email : "+principal.getUser().getEmail());
        System.out.println("User-picture : "+principal.getUser().getPicture());
        // iterator 순차 출력 해보기
        Iterator<? extends GrantedAuthority> iter = principal.getAuthorities().iterator();
        while (iter.hasNext()) {
            GrantedAuthority auth = iter.next();
            System.out.println(auth.getAuthority());
        }

        return "유저 페이지입니다.";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    /*
    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "어드민 페이지입니다.";
    }

    @GetMapping("/join")
    public String join() {
        return "join";
    }

*/
    private final UserService userService;
    @GetMapping("users/login/naver")
    public void loginNaver(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
        String url = userService.getNaverAuthorizeUrl();
        try {
            response.sendRedirect(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/user-info")
    public UserDto.UserResponse getUser(@AuthenticationPrincipal PrincipalDetails principal) {
        //System.out.println("Principal : " + principal);
        System.out.println("OAuth2 : "+principal.getUser().getProvider());
        System.out.println("User-name : "+principal.getUser().getUsername());
        System.out.println("User-email : "+principal.getUser().getEmail());
        System.out.println("User-picture : "+principal.getUser().getPicture());
        // iterator 순차 출력 해보기
        Iterator<? extends GrantedAuthority> iter = principal.getAuthorities().iterator();
        while (iter.hasNext()) {
            GrantedAuthority auth = iter.next();
            System.out.println(auth.getAuthority());
        }
        User principalUser = principal.getUser();
        User user = User.builder()
                .email(principalUser.getEmail())
                .picture(principalUser.getPicture())
                .provider(principalUser.getProvider())
                .username(principalUser.getUsername())
                .build();

        return new UserDto.UserResponse(user);
    }
//    @GetMapping("users/login/naver/callback")
//    public void naverLoginCallback(HttpServletRequest request, HttpServletResponse response) {
//        String code = request.getParameter("code"); // 인증 코드
//        String state = request.getParameter("state"); // 난수 상태 값
//
//        // 인증 코드와 난수 상태 값을 사용하여 사용자 정보를 요청하고 처리하는 로직을 작성
//        // 예를 들어 userService를 사용하여 사용자 정보를 처리할 수 있습니다.
//
//        // 처리가 끝난 후, 사용자에게 보여줄 페이지로 리다이렉트
//        try {
//            response.sendRedirect("/");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    @GetMapping("/oauth/login")
//    public void callBack(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, UnsupportedEncodingException, URISyntaxException, JsonProcessingException {
//
//        if (callback.getError() != null) {
//            System.out.println(callback.getError_description());
//        }
//
//        String responseToken = loginService.getNaverTokenUrl("token", callback);
//
//        ObjectMapper mapper = new ObjectMapper();
//        NaverToken token = mapper.readValue(responseToken, NaverToken.class);
//        String responseUser = loginService.getNaverUserByToken(token);
//        NaverRes naverUser = mapper.readValue(responseUser, NaverRes.class);
//
//        System.out.println("naverUser.toString() : " + naverUser.toString());
//        System.out.println("naverUser.getResonse().getGender() : " + naverUser.getResponse().getGender());
//        System.out.println("naverUser.getResonse().getBirthyear() : " + naverUser.getResponse().getBirthyear());
//        System.out.println("naverUser.getResonse().getAge() : " + naverUser.getResponse().getAge());
//
//    }
}