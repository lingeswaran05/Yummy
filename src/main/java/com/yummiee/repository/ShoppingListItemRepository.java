package com.yummiee.repository;

import com.yummiee.entity.ShoppingListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Long> {

    List<ShoppingListItem> findByUserId(Long userId);

    Optional<ShoppingListItem> findByIdAndUserId(Long id, Long userId);

    Optional<ShoppingListItem> findByUserIdAndNameIgnoreCaseAndUnit(Long userId, String name, String unit);

    void deleteByUserId(Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    void deleteByRecipeId(Long recipeId);
}
