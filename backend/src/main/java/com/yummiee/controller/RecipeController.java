package com.yummiee.controller;

import com.yummiee.dto.RecipeRequest;
import com.yummiee.dto.RecipeResponse;
import com.yummiee.dto.RecipeSummaryResponse;
import com.yummiee.security.ClerkUserPrincipal;
import com.yummiee.service.RecipeService;
import com.yummiee.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public ResponseEntity<List<RecipeSummaryResponse>> getAllRecipes(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String sort) {
        List<RecipeSummaryResponse> recipes = recipeService.getAllRecipes(search, category, difficulty, sort);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> getRecipeById(@PathVariable Long id) {
        RecipeResponse recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(recipe);
    }

    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(
            @Valid @RequestBody RecipeRequest request,
            @AuthenticationPrincipal ClerkUserPrincipal principal) {
        User currentUser = principal.getUser();
        RecipeResponse response = recipeService.createRecipe(request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody RecipeRequest request,
            @AuthenticationPrincipal ClerkUserPrincipal principal) {
        User currentUser = principal.getUser();
        RecipeResponse response = recipeService.updateRecipe(id, request, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(
            @PathVariable Long id,
            @AuthenticationPrincipal ClerkUserPrincipal principal) {
        User currentUser = principal.getUser();
        recipeService.deleteRecipe(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
