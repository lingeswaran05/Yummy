package com.yummiee.controller;

import com.yummiee.dto.RecipeSummaryResponse;
import com.yummiee.security.ClerkUserPrincipal;
import com.yummiee.service.WishlistService;
import com.yummiee.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    public ResponseEntity<List<RecipeSummaryResponse>> getWishlist(
            @AuthenticationPrincipal ClerkUserPrincipal principal) {
        User currentUser = principal.getUser();
        List<RecipeSummaryResponse> wishlist = wishlistService.getWishlist(currentUser);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("/{recipeId}")
    public ResponseEntity<RecipeSummaryResponse> addToWishlist(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal ClerkUserPrincipal principal) {
        User currentUser = principal.getUser();
        RecipeSummaryResponse response = wishlistService.addToWishlist(recipeId, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> removeFromWishlist(
            @PathVariable Long recipeId,
            @AuthenticationPrincipal ClerkUserPrincipal principal) {
        User currentUser = principal.getUser();
        wishlistService.removeFromWishlist(recipeId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
