package com.yummiee;

import com.yummiee.dto.RecipeRequest;
import com.yummiee.dto.RecipeResponse;
import com.yummiee.dto.RecipeSummaryResponse;
import com.yummiee.entity.Recipe;
import com.yummiee.exception.ForbiddenException;
import com.yummiee.exception.ResourceNotFoundException;
import com.yummiee.repository.RecipeRepository;
import com.yummiee.repository.ShoppingListItemRepository;
import com.yummiee.repository.WishlistRepository;
import com.yummiee.service.RecipeService;
import com.yummiee.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private ShoppingListItemRepository shoppingListItemRepository;

    @InjectMocks
    private RecipeService recipeService;

    private User user1;
    private User user2;
    private Recipe recipe1;

    @BeforeEach
    void setUp() {
        user1 = new User("user1_clerk", "user1@example.com", "User", "One", null);
        user1.setId(1L);

        user2 = new User("user2_clerk", "user2@example.com", "User", "Two", null);
        user2.setId(2L);

        recipe1 = new Recipe();
        recipe1.setId(100L);
        recipe1.setUser(user1);
        recipe1.setName("Creamy Garlic Pasta");
        recipe1.setDescription("Delicious pasta");
        recipe1.setCategory("Dinner");
        recipe1.setTimeMinutes(20);
        recipe1.setDifficulty("Easy");
        recipe1.setServings(2);
    }

    @Test
    void testGetAllRecipes() {
        when(recipeRepository.searchRecipes("pasta", "Dinner", "Easy"))
                .thenReturn(new ArrayList<>(List.of(recipe1)));

        List<RecipeSummaryResponse> result = recipeService.getAllRecipes("pasta", "Dinner", "Easy", "Recently Added");

        assertEquals(1, result.size());
        assertEquals("Creamy Garlic Pasta", result.get(0).getName());
        verify(recipeRepository, times(1)).searchRecipes("pasta", "Dinner", "Easy");
    }

    @Test
    void testGetRecipeById_Success() {
        when(recipeRepository.findById(100L)).thenReturn(Optional.of(recipe1));

        RecipeResponse response = recipeService.getRecipeById(100L);

        assertNotNull(response);
        assertEquals("Creamy Garlic Pasta", response.getName());
    }

    @Test
    void testGetRecipeById_NotFound() {
        when(recipeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recipeService.getRecipeById(999L));
    }

    @Test
    void testCreateRecipe() {
        RecipeRequest request = new RecipeRequest();
        request.setName("New Recipe");
        request.setDescription("Desc");
        request.setCategory("Lunch");
        request.setTime(15);
        request.setDifficulty("Easy");
        request.setServings(4);

        when(recipeRepository.save(any(Recipe.class))).thenAnswer(invocation -> {
            Recipe r = invocation.getArgument(0);
            r.setId(200L);
            return r;
        });

        RecipeResponse response = recipeService.createRecipe(request, user1);

        assertNotNull(response);
        assertEquals(200L, response.getId());
        assertEquals("New Recipe", response.getName());
    }

    @Test
    void testUpdateRecipe_OwnRecipe_Success() {
        RecipeRequest request = new RecipeRequest();
        request.setName("Updated Pasta");
        request.setDescription("Updated Desc");
        request.setCategory("Dinner");
        request.setTime(25);
        request.setDifficulty("Medium");
        request.setServings(3);

        when(recipeRepository.findById(100L)).thenReturn(Optional.of(recipe1));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe1);

        RecipeResponse response = recipeService.updateRecipe(100L, request, user1);

        assertNotNull(response);
        assertEquals("Updated Pasta", recipe1.getName());
    }

    @Test
    void testUpdateRecipe_AnotherUser_Forbidden() {
        RecipeRequest request = new RecipeRequest();
        request.setName("Hacked Pasta");

        when(recipeRepository.findById(100L)).thenReturn(Optional.of(recipe1));

        assertThrows(ForbiddenException.class, () -> recipeService.updateRecipe(100L, request, user2));
    }

    @Test
    void testDeleteRecipe_OwnRecipe_Success() {
        when(recipeRepository.findById(100L)).thenReturn(Optional.of(recipe1));

        recipeService.deleteRecipe(100L, user1);

        verify(wishlistRepository, times(1)).deleteByRecipeId(100L);
        verify(shoppingListItemRepository, times(1)).deleteByRecipeId(100L);
        verify(recipeRepository, times(1)).delete(recipe1);
    }

    @Test
    void testDeleteRecipe_AnotherUser_Forbidden() {
        when(recipeRepository.findById(100L)).thenReturn(Optional.of(recipe1));

        assertThrows(ForbiddenException.class, () -> recipeService.deleteRecipe(100L, user2));
    }
}
