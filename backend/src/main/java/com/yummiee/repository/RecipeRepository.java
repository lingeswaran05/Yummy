package com.yummiee.repository;

import com.yummiee.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT DISTINCT r FROM Recipe r LEFT JOIN r.ingredients i " +
           "WHERE (:search IS NULL OR :search = '' OR " +
           "      LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "      LOWER(r.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "      LOWER(r.category) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "      LOWER(i.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:category IS NULL OR :category = '' OR :category = 'All' OR LOWER(r.category) = LOWER(:category)) " +
           "AND (:difficulty IS NULL OR :difficulty = '' OR LOWER(r.difficulty) = LOWER(:difficulty))")
    List<Recipe> searchRecipes(
            @Param("search") String search,
            @Param("category") String category,
            @Param("difficulty") String difficulty
    );

    List<Recipe> findByUserId(Long userId);
}
