package com.spring.chromavoyage.api.oauth2.controller;

import com.spring.chromavoyage.api.oauth2.config.auth.PrincipalDetails;
import com.spring.chromavoyage.api.oauth2.model.User;
import com.spring.chromavoyage.api.oauth2.model.UserDto;

import com.spring.chromavoyage.api.oauth2.repository.UserRepository;
import com.spring.chromavoyage.api.oauth2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @GetMapping({ "", "/" })
    public @ResponseBody String main() {
        return "메인 페이지입니다.";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principal) {

        System.out.println("OAuth2 : "+principal.getUser().getProvider());
        System.out.println("User-name : "+principal.getUser().getUsername());
        System.out.println("User-email : "+principal.getUser().getEmail());
        System.out.println("User-picture : "+principal.getUser().getPicture());

        Iterator<? extends GrantedAuthority> iter = principal.getAuthorities().iterator();
        while (iter.hasNext()) {
            GrantedAuthority auth = iter.next();
            System.out.println(auth.getAuthority());
        }

        return "유저 페이지입니다.";
    }
    private final UserService userService;
    @GetMapping("/login")
    public void loginGoole(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
        String url = userService.getGoogleAuthorizeUrl();
        try {
            response.sendRedirect(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    //private final UserService userService;
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
}