package com.yummiee;

import com.yummiee.dto.ShoppingListItemRequest;
import com.yummiee.dto.ShoppingListItemResponse;
import com.yummiee.entity.ShoppingListItem;
import com.yummiee.exception.ForbiddenException;
import com.yummiee.repository.RecipeRepository;
import com.yummiee.repository.ShoppingListItemRepository;
import com.yummiee.service.ShoppingListService;
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
public class ShoppingListServiceTest {

    @Mock
    private ShoppingListItemRepository shoppingListItemRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private ShoppingListService shoppingListService;

    private User user1;
    private User user2;
    private ShoppingListItem item1;

    @BeforeEach
    void setUp() {
        user1 = new User("user1_clerk", "user1@example.com", "User", "One", null);
        user1.setId(1L);

        user2 = new User("user2_clerk", "user2@example.com", "User", "Two", null);
        user2.setId(2L);

        item1 = new ShoppingListItem(user1, null, "Pasta", 200.0, "g", false);
        item1.setId(10L);
    }

    @Test
    void testGetShoppingList() {
        when(shoppingListItemRepository.findByUserId(1L)).thenReturn(List.of(item1));

        List<ShoppingListItemResponse> result = shoppingListService.getShoppingList(user1);

        assertEquals(1, result.size());
        assertEquals("Pasta", result.get(0).getName());
        assertEquals(200.0, result.get(0).getQuantity());
    }

    @Test
    void testAddShoppingListItem_NewItem() {
        ShoppingListItemRequest request = new ShoppingListItemRequest("Garlic", 4.0, "cloves", false, null);

        when(shoppingListItemRepository.findByUserIdAndNameIgnoreCaseAndUnit(1L, "Garlic", "cloves"))
                .thenReturn(Optional.empty());
        when(shoppingListItemRepository.save(any(ShoppingListItem.class))).thenAnswer(i -> {
            ShoppingListItem s = i.getArgument(0);
            s.setId(11L);
            return s;
        });

        ShoppingListItemResponse response = shoppingListService.addShoppingListItem(request, user1);

        assertNotNull(response);
        assertEquals(11L, response.getId());
        assertEquals("Garlic", response.getName());
    }

    @Test
    void testAddShoppingListItem_MergeDuplicate() {
        ShoppingListItemRequest request = new ShoppingListItemRequest("Pasta", 100.0, "g", false, null);

        when(shoppingListItemRepository.findByUserIdAndNameIgnoreCaseAndUnit(1L, "Pasta", "g"))
                .thenReturn(Optional.of(item1));
        when(shoppingListItemRepository.save(any(ShoppingListItem.class))).thenReturn(item1);

        ShoppingListItemResponse response = shoppingListService.addShoppingListItem(request, user1);

        assertEquals(300.0, item1.getQuantity());
        assertEquals(300.0, response.getQuantity());
    }

    @Test
    void testUpdateShoppingListItem_ToggleChecked_Success() {
        ShoppingListItemRequest request = new ShoppingListItemRequest();
        request.setChecked(true);

        when(shoppingListItemRepository.findById(10L)).thenReturn(Optional.of(item1));
        when(shoppingListItemRepository.save(any(ShoppingListItem.class))).thenReturn(item1);

        ShoppingListItemResponse response = shoppingListService.updateShoppingListItem(10L, request, user1);

        assertTrue(response.getChecked());
    }

    @Test
    void testUpdateShoppingListItem_AnotherUser_Forbidden() {
        ShoppingListItemRequest request = new ShoppingListItemRequest();
        request.setChecked(true);

        when(shoppingListItemRepository.findById(10L)).thenReturn(Optional.of(item1));

        assertThrows(ForbiddenException.class, () -> shoppingListService.updateShoppingListItem(10L, request, user2));
    }

    @Test
    void testDeleteShoppingListItem_Success() {
        when(shoppingListItemRepository.findById(10L)).thenReturn(Optional.of(item1));

        shoppingListService.deleteShoppingListItem(10L, user1);

        verify(shoppingListItemRepository, times(1)).delete(item1);
    }

    @Test
    void testClearShoppingList() {
        shoppingListService.clearShoppingList(user1);

        verify(shoppingListItemRepository, times(1)).deleteByUserId(1L);
    }
}
