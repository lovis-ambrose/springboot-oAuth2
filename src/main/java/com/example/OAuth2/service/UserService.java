package com.example.OAuth2.service;

import com.example.OAuth2.model.UserInfo;
import com.example.OAuth2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Map<String, Object> getUserName(@AuthenticationPrincipal OAuth2User principal) {
        System.out.println(principal.getAttributes());
//        System.out.println( SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    //save user to database
    public ResponseEntity<String> saveUser(@AuthenticationPrincipal OAuth2User user){
        String email = user.getAttribute("email");
        String fullName = user.getAttribute("name");
        String username = user.getAttribute("login");
        String avatar = user.getAttribute("avatar_url");
        String bio = user.getAttribute("bio");

        //get userinfo from db if exists
        UserInfo userInfo = userRepository.findByEmail(email);

        // Create new UserInfo object if it doesn't exist
        if (userInfo == null) {
            userInfo = new UserInfo();
        }

        // Update UserInfo with OAuth2 user attributes
        userInfo.setEmail(email);
        userInfo.setFullName(fullName);
        userInfo.setUsername(username);
        userInfo.setAvatar(avatar);
        userInfo.setBio(bio);

       userRepository.save(userInfo);
       return ResponseEntity.ok("User saved Successfully");

    }

    public String anyError(HttpServletRequest request) {
        String message = (String) request.getSession().getAttribute("error.message");
        request.getSession().removeAttribute("error.message");
        return message;
    }
}
