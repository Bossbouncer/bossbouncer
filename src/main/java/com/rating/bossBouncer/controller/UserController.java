package com.rating.bossBouncer.controller;

import com.rating.bossBouncer.entity.User;
import com.rating.bossBouncer.exceptions.ResourceNotFoundException;
import com.rating.bossBouncer.repository.UserRepository;
import com.rating.bossBouncer.security.CurrentUser;
import com.rating.bossBouncer.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
