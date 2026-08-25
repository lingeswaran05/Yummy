package com.yummiee.controller;

import com.yummiee.security.ClerkUserPrincipal;
import com.yummiee.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(
            @AuthenticationPrincipal ClerkUserPrincipal principal) {
        User user = principal.getUser();
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "clerkUserId", user.getClerkUserId(),
                "email", user.getEmail() != null ? user.getEmail() : "",
                "firstName", user.getFirstName() != null ? user.getFirstName() : "",
                "lastName", user.getLastName() != null ? user.getLastName() : "",
                "imageUrl", user.getImageUrl() != null ? user.getImageUrl() : ""
        ));
    }
}
