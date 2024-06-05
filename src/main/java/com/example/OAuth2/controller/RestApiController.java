package com.example.OAuth2.controller;

import com.example.OAuth2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class RestApiController {
    private final UserService userService;
    @Autowired
    public RestApiController(UserService userService){
        this.userService = userService;
    }
    @RequestMapping("/user")
    public Map<String, Object> getUser(@AuthenticationPrincipal OAuth2User user){
        return userService.getUserName(user);
    }

    @PostMapping("/save-user")
    public ResponseEntity<String> saveUserInfo(@AuthenticationPrincipal OAuth2User user) {
        return userService.saveUser(user);
    }



    @GetMapping("/error")
    public String raiseAnyError(HttpServletRequest error){
        return userService.anyError(error);
    }
}
