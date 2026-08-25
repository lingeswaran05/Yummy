package com.yummiee.service;

import com.yummiee.dto.ShoppingListItemRequest;
import com.yummiee.dto.ShoppingListItemResponse;
import com.yummiee.entity.Recipe;
import com.yummiee.entity.ShoppingListItem;
import com.yummiee.exception.ForbiddenException;
import com.yummiee.exception.ResourceNotFoundException;
import com.yummiee.repository.RecipeRepository;
import com.yummiee.repository.ShoppingListItemRepository;
import com.yummiee.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShoppingListService {

    private final ShoppingListItemRepository shoppingListItemRepository;
    private final RecipeRepository recipeRepository;

    public ShoppingListService(ShoppingListItemRepository shoppingListItemRepository, RecipeRepository recipeRepository) {
        this.shoppingListItemRepository = shoppingListItemRepository;
        this.recipeRepository = recipeRepository;
    }

    @Transactional(readOnly = true)
    public List<ShoppingListItemResponse> getShoppingList(User currentUser) {
        List<ShoppingListItem> items = shoppingListItemRepository.findByUserId(currentUser.getId());
        return items.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ShoppingListItemResponse addShoppingListItem(ShoppingListItemRequest request, User currentUser) {
        Recipe recipe = null;
        if (request.getRecipeId() != null) {
            recipe = recipeRepository.findById(request.getRecipeId()).orElse(null);
        }

        // Duplicate ingredient merging logic (same name case-insensitive & same unit)
        Optional<ShoppingListItem> existingOpt = shoppingListItemRepository
                .findByUserIdAndNameIgnoreCaseAndUnit(currentUser.getId(), request.getName().trim(), request.getUnit().trim());

        ShoppingListItem item;
        if (existingOpt.isPresent()) {
            item = existingOpt.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            if (recipe != null) {
                item.setRecipe(recipe);
            }
        } else {
            item = new ShoppingListItem(
                    currentUser,
                    recipe,
                    request.getName().trim(),
                    request.getQuantity(),
                    request.getUnit().trim(),
                    request.getChecked() != null ? request.getChecked() : false
            );
        }

        ShoppingListItem savedItem = shoppingListItemRepository.save(item);
        return mapToResponse(savedItem);
    }

    @Transactional
    public ShoppingListItemResponse updateShoppingListItem(Long id, ShoppingListItemRequest request, User currentUser) {
        ShoppingListItem item = shoppingListItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shopping list item not found with id: " + id));

        if (!item.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You do not have permission to modify this shopping list item");
        }

        if (request.getChecked() != null) {
            item.setChecked(request.getChecked());
        }
        if (request.getQuantity() != null) {
            item.setQuantity(request.getQuantity());
        }
        if (request.getName() != null && !request.getName().isBlank()) {
            item.setName(request.getName().trim());
        }
        if (request.getUnit() != null && !request.getUnit().isBlank()) {
            item.setUnit(request.getUnit().trim());
        }

        ShoppingListItem updatedItem = shoppingListItemRepository.save(item);
        return mapToResponse(updatedItem);
    }

    @Transactional
    public void deleteShoppingListItem(Long id, User currentUser) {
        ShoppingListItem item = shoppingListItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shopping list item not found with id: " + id));

        if (!item.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You do not have permission to modify this shopping list item");
        }

        shoppingListItemRepository.delete(item);
    }

    @Transactional
    public void clearShoppingList(User currentUser) {
        shoppingListItemRepository.deleteByUserId(currentUser.getId());
    }

    private ShoppingListItemResponse mapToResponse(ShoppingListItem item) {
        return new ShoppingListItemResponse(
                item.getId(),
                item.getName(),
                item.getQuantity(),
                item.getUnit(),
                item.getChecked(),
                item.getRecipe() != null ? item.getRecipe().getId() : null,
                item.getRecipe() != null ? item.getRecipe().getName() : null
        );
    }
}
