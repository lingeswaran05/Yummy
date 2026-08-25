package com.yummiee.service;

import com.yummiee.dto.RecipeSummaryResponse;
import com.yummiee.entity.Recipe;
import com.yummiee.entity.Wishlist;
import com.yummiee.exception.ResourceAlreadyExistsException;
import com.yummiee.exception.ResourceNotFoundException;
import com.yummiee.repository.RecipeRepository;
import com.yummiee.repository.WishlistRepository;
import com.yummiee.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final RecipeRepository recipeRepository;

    public WishlistService(WishlistRepository wishlistRepository, RecipeRepository recipeRepository) {
        this.wishlistRepository = wishlistRepository;
        this.recipeRepository = recipeRepository;
    }

    @Transactional(readOnly = true)
    public List<RecipeSummaryResponse> getWishlist(User currentUser) {
        List<Wishlist> wishlists = wishlistRepository.findByUserId(currentUser.getId());
        return wishlists.stream()
                .map(w -> mapToSummaryResponse(w.getRecipe()))
                .collect(Collectors.toList());
    }

    @Transactional
    public RecipeSummaryResponse addToWishlist(Long recipeId, User currentUser) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + recipeId));

        if (wishlistRepository.existsByUserIdAndRecipeId(currentUser.getId(), recipeId)) {
            throw new ResourceAlreadyExistsException("Recipe is already in your wishlist");
        }

        Wishlist wishlist = new Wishlist(currentUser, recipe);
        wishlistRepository.save(wishlist);

        return mapToSummaryResponse(recipe);
    }

    @Transactional
    public void removeFromWishlist(Long recipeId, User currentUser) {
        wishlistRepository.deleteByUserIdAndRecipeId(currentUser.getId(), recipeId);
    }

    private RecipeSummaryResponse mapToSummaryResponse(Recipe recipe) {
        return new RecipeSummaryResponse(
                recipe.getId(),
                recipe.getName(),
                recipe.getTimeMinutes(),
                recipe.getServings(),
                recipe.getCategory(),
                recipe.getImageUrl()
        );
    }
}
