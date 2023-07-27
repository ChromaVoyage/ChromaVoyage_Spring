package com.spring.chromavoyage.api.oauth2.controller;

import com.spring.chromavoyage.api.oauth2.config.auth.PrincipalDetails;
import com.spring.chromavoyage.api.oauth2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.Iterator;

@Controller // view return
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
   @GetMapping("/login")
    public String login(@AuthenticationPrincipal PrincipalDetails principal, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(principal);
        return "login";
    }
}