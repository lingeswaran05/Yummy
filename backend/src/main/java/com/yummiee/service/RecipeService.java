package com.yummiee.service;

import com.yummiee.dto.*;
import com.yummiee.entity.*;
import com.yummiee.exception.ForbiddenException;
import com.yummiee.exception.ResourceNotFoundException;
import com.yummiee.repository.*;
import com.yummiee.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final WishlistRepository wishlistRepository;
    private final ShoppingListItemRepository shoppingListItemRepository;

    public RecipeService(
            RecipeRepository recipeRepository,
            WishlistRepository wishlistRepository,
            ShoppingListItemRepository shoppingListItemRepository) {
        this.recipeRepository = recipeRepository;
        this.wishlistRepository = wishlistRepository;
        this.shoppingListItemRepository = shoppingListItemRepository;
    }

    @Transactional(readOnly = true)
    public List<RecipeSummaryResponse> getAllRecipes(String search, String category, String difficulty, String sort) {
        List<Recipe> recipes = recipeRepository.searchRecipes(search, category, difficulty);

        if ("Quickest".equalsIgnoreCase(sort)) {
            recipes.sort(Comparator.comparing(Recipe::getTimeMinutes, Comparator.nullsLast(Comparator.naturalOrder())));
        } else if ("Most Liked".equalsIgnoreCase(sort)) {
            recipes.sort(Comparator.comparing(Recipe::getRating, Comparator.nullsLast(Comparator.reverseOrder()))
                    .thenComparing(Recipe::getReviewCount, Comparator.nullsLast(Comparator.reverseOrder())));
        } else {
            // Default: "Recently Added"
            recipes.sort(Comparator.comparing(Recipe::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())));
        }

        return recipes.stream()
                .map(this::mapToSummaryResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RecipeResponse getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));

        return mapToDetailResponse(recipe);
    }

    @Transactional
    public RecipeResponse createRecipe(RecipeRequest request, User currentUser) {
        Recipe recipe = new Recipe();
        recipe.setUser(currentUser);
        mapRequestToEntity(request, recipe);

        Recipe savedRecipe = recipeRepository.save(recipe);
        return mapToDetailResponse(savedRecipe);
    }

    @Transactional
    public RecipeResponse updateRecipe(Long id, RecipeRequest request, User currentUser) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));

        if (!recipe.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You do not have permission to modify this recipe");
        }

        mapRequestToEntity(request, recipe);
        Recipe updatedRecipe = recipeRepository.save(recipe);
        return mapToDetailResponse(updatedRecipe);
    }

    @Transactional
    public void deleteRecipe(Long id, User currentUser) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));

        if (!recipe.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You do not have permission to modify this recipe");
        }

        wishlistRepository.deleteByRecipeId(id);
        shoppingListItemRepository.deleteByRecipeId(id);
        recipeRepository.delete(recipe);
    }

    private void mapRequestToEntity(RecipeRequest request, Recipe recipe) {
        recipe.setName(request.getName());
        recipe.setDescription(request.getDescription());
        recipe.setCategory(request.getCategory());
        recipe.setTimeMinutes(request.getTime());
        recipe.setDifficulty(request.getDifficulty());
        recipe.setServings(request.getServings());
        recipe.setImageUrl(request.getImage());
        recipe.setNotes(request.getNotes());

        // Update Ingredients
        recipe.getIngredients().clear();
        if (request.getIngredients() != null) {
            for (IngredientRequest ingReq : request.getIngredients()) {
                Ingredient ingredient = new Ingredient(ingReq.getName(), ingReq.getQuantity(), ingReq.getUnit());
                recipe.addIngredient(ingredient);
            }
        }

        // Update Instructions
        recipe.getInstructions().clear();
        if (request.getInstructions() != null) {
            for (InstructionRequest instReq : request.getInstructions()) {
                Instruction instruction = new Instruction(instReq.getStep(), instReq.getTitle(), instReq.getDescription());
                recipe.addInstruction(instruction);
            }
        }

        // Update Nutrition
        if (request.getNutrition() != null) {
            NutritionRequest nutReq = request.getNutrition();
            Nutrition nutrition = recipe.getNutrition();
            if (nutrition == null) {
                nutrition = new Nutrition();
            }
            nutrition.setCalories(nutReq.getCalories());
            nutrition.setProtein(nutReq.getProtein());
            nutrition.setCarbs(nutReq.getCarbs());
            nutrition.setFat(nutReq.getFat());
            recipe.setNutrition(nutrition);
        } else {
            recipe.setNutrition(null);
        }
    }

    public RecipeSummaryResponse mapToSummaryResponse(Recipe recipe) {
        return new RecipeSummaryResponse(
                recipe.getId(),
                recipe.getName(),
                recipe.getTimeMinutes(),
                recipe.getServings(),
                recipe.getCategory(),
                recipe.getImageUrl()
        );
    }

    private RecipeResponse mapToDetailResponse(Recipe recipe) {
        RecipeResponse response = new RecipeResponse();
        response.setId(recipe.getId());
        response.setName(recipe.getName());
        response.setDescription(recipe.getDescription());
        response.setImage(recipe.getImageUrl());
        response.setCategory(recipe.getCategory());
        response.setTime(recipe.getTimeMinutes());
        response.setDifficulty(recipe.getDifficulty());
        response.setServings(recipe.getServings());
        response.setRating(recipe.getRating() != null ? recipe.getRating() : 0.0);
        response.setReviews(recipe.getReviewCount() != null ? recipe.getReviewCount() : 0);
        response.setNotes(recipe.getNotes());

        if (recipe.getIngredients() != null) {
            response.setIngredients(recipe.getIngredients().stream()
                    .map(i -> new IngredientResponse(i.getId(), i.getName(), i.getQuantity(), i.getUnit()))
                    .collect(Collectors.toList()));
        }

        if (recipe.getInstructions() != null) {
            response.setInstructions(recipe.getInstructions().stream()
                    .map(i -> new InstructionResponse(i.getId(), i.getStepNumber(), i.getTitle(), i.getDescription()))
                    .sorted(Comparator.comparing(InstructionResponse::getStep))
                    .collect(Collectors.toList()));
        }

        if (recipe.getNutrition() != null) {
            Nutrition n = recipe.getNutrition();
            response.setNutrition(new NutritionResponse(n.getCalories(), n.getProtein(), n.getCarbs(), n.getFat()));
        }

        return response;
    }
}
