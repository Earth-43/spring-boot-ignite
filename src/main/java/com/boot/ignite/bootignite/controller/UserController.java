package com.boot.ignite.bootignite.controller;

import com.boot.ignite.bootignite.dto.User;
import com.boot.ignite.bootignite.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/v1/user")
    public User getUser(HttpServletRequest httpRequest) {
        return userService.getUser();
    }
}
