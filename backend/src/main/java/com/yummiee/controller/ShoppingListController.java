package com.yummiee.controller;

import com.yummiee.dto.ShoppingListItemRequest;
import com.yummiee.dto.ShoppingListItemResponse;
import com.yummiee.security.ClerkUserPrincipal;
import com.yummiee.service.ShoppingListService;
import com.yummiee.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping-list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping
    public ResponseEntity<List<ShoppingListItemResponse>> getShoppingList(
            @AuthenticationPrincipal ClerkUserPrincipal principal) {
        User currentUser = principal.getUser();
        List<ShoppingListItemResponse> list = shoppingListService.getShoppingList(currentUser);
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<ShoppingListItemResponse> addShoppingListItem(
            @Valid @RequestBody ShoppingListItemRequest request,
            @AuthenticationPrincipal ClerkUserPrincipal principal) {
        User currentUser = principal.getUser();
        ShoppingListItemResponse response = shoppingListService.addShoppingListItem(request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShoppingListItemResponse> updateShoppingListItem(
            @PathVariable Long id,
            @RequestBody ShoppingListItemRequest request,
            @AuthenticationPrincipal ClerkUserPrincipal principal) {
        User currentUser = principal.getUser();
        ShoppingListItemResponse response = shoppingListService.updateShoppingListItem(id, request, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoppingListItem(
            @PathVariable Long id,
            @AuthenticationPrincipal ClerkUserPrincipal principal) {
        User currentUser = principal.getUser();
        shoppingListService.deleteShoppingListItem(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearShoppingList(
            @AuthenticationPrincipal ClerkUserPrincipal principal) {
        User currentUser = principal.getUser();
        shoppingListService.clearShoppingList(currentUser);
        return ResponseEntity.noContent().build();
    }
}
