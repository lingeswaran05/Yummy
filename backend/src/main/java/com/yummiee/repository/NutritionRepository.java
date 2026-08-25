package com.yummiee.repository;

import com.yummiee.entity.Nutrition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutritionRepository extends JpaRepository<Nutrition, Long> {
    Optional<Nutrition> findByRecipeId(Long recipeId);
    void deleteByRecipeId(Long recipeId);
}
