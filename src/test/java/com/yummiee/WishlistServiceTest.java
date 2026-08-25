package com.yummiee;

import com.yummiee.dto.RecipeSummaryResponse;
import com.yummiee.entity.Recipe;
import com.yummiee.entity.Wishlist;
import com.yummiee.exception.ResourceAlreadyExistsException;
import com.yummiee.exception.ResourceNotFoundException;
import com.yummiee.repository.RecipeRepository;
import com.yummiee.repository.WishlistRepository;
import com.yummiee.service.WishlistService;
import com.yummiee.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private WishlistService wishlistService;

    private User user1;
    private Recipe recipe1;

    @BeforeEach
    void setUp() {
        user1 = new User("user1_clerk", "user1@example.com", "User", "One", null);
        user1.setId(1L);

        recipe1 = new Recipe();
        recipe1.setId(100L);
        recipe1.setName("Creamy Garlic Pasta");
        recipe1.setTimeMinutes(20);
        recipe1.setServings(2);
        recipe1.setCategory("Dinner");
        recipe1.setImageUrl("http://img");
    }

    @Test
    void testGetWishlist() {
        Wishlist wishlist = new Wishlist(user1, recipe1);
        when(wishlistRepository.findByUserId(1L)).thenReturn(List.of(wishlist));

        List<RecipeSummaryResponse> result = wishlistService.getWishlist(user1);

        assertEquals(1, result.size());
        assertEquals("Creamy Garlic Pasta", result.get(0).getName());
    }

    @Test
    void testAddToWishlist_Success() {
        when(recipeRepository.findById(100L)).thenReturn(Optional.of(recipe1));
        when(wishlistRepository.existsByUserIdAndRecipeId(1L, 100L)).thenReturn(false);

        RecipeSummaryResponse response = wishlistService.addToWishlist(100L, user1);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Creamy Garlic Pasta", response.getName());
        verify(wishlistRepository, times(1)).save(any(Wishlist.class));
    }

    @Test
    void testAddToWishlist_Duplicate_ThrowsConflict() {
        when(recipeRepository.findById(100L)).thenReturn(Optional.of(recipe1));
        when(wishlistRepository.existsByUserIdAndRecipeId(1L, 100L)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> wishlistService.addToWishlist(100L, user1));
    }

    @Test
    void testAddToWishlist_RecipeNotFound_ThrowsNotFound() {
        when(recipeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> wishlistService.addToWishlist(999L, user1));
    }

    @Test
    void testRemoveFromWishlist() {
        wishlistService.removeFromWishlist(100L, user1);
        verify(wishlistRepository, times(1)).deleteByUserIdAndRecipeId(1L, 100L);
    }
}
