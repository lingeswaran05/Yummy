package com.yummiee;

import com.yummiee.entity.*;
import com.yummiee.repository.RecipeRepository;
import com.yummiee.user.User;
import com.yummiee.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class YummieeApplication {

    public static void main(String[] args) {
        SpringApplication.run(YummieeApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedDatabase(UserRepository userRepository, RecipeRepository recipeRepository) {
        return args -> {
            if (recipeRepository.count() == 0) {
                User demoUser = userRepository.findByClerkUserId("user_demo")
                        .orElseGet(() -> userRepository.save(new User(
                                "user_demo",
                                "chef@yummiee.com",
                                "Chef",
                                "Gordon",
                                "https://images.unsplash.com/photo-1577219491135-ce391730fb2c?w=150"
                        )));

                // Recipe 1: Creamy Garlic Pasta
                Recipe recipe1 = new Recipe();
                recipe1.setUser(demoUser);
                recipe1.setName("Creamy Garlic Pasta");
                recipe1.setDescription("A rich, silky pasta coated in a savory garlic cream sauce with fresh parmesan and herbs.");
                recipe1.setCategory("Dinner");
                recipe1.setTimeMinutes(20);
                recipe1.setDifficulty("Easy");
                recipe1.setServings(2);
                recipe1.setImageUrl("https://images.unsplash.com/photo-1621996346565-e3d5d6281313?q=80&w=800&auto=format&fit=crop");
                recipe1.setRating(4.8);
                recipe1.setReviewCount(124);
                recipe1.setNotes("For extra flavor, reserve 1/2 cup of pasta cooking water to thin out the sauce if it gets too thick.");

                recipe1.addIngredient(new Ingredient("Fettuccine or Penne", 200.0, "g"));
                recipe1.addIngredient(new Ingredient("Heavy Cream", 150.0, "ml"));
                recipe1.addIngredient(new Ingredient("Garlic Cloves", 4.0, "cloves"));
                recipe1.addIngredient(new Ingredient("Parmesan Cheese", 50.0, "g"));
                recipe1.addIngredient(new Ingredient("Unsalted Butter", 2.0, "tbsp"));
                recipe1.addIngredient(new Ingredient("Fresh Parsley", 1.0, "handful"));

                recipe1.addInstruction(new Instruction(1, "Boil Pasta", "Bring a large pot of salted water to a boil. Cook pasta according to package instructions until al dente. Reserve 1/2 cup pasta water before draining."));
                recipe1.addInstruction(new Instruction(2, "Sauté Garlic", "In a large skillet over medium heat, melt the butter. Add finely minced garlic and sauté for 1-2 minutes until fragrant but not browned."));
                recipe1.addInstruction(new Instruction(3, "Make Sauce", "Pour in heavy cream and bring to a gentle simmer. Reduce heat to low and stir in grated parmesan cheese until melted and smooth."));
                recipe1.addInstruction(new Instruction(4, "Combine & Serve", "Toss cooked pasta into the sauce until evenly coated. Garnish with freshly chopped parsley and extra parmesan before serving."));

                recipe1.setNutrition(new Nutrition(520, 14.0, 58.0, 26.0));
                recipeRepository.save(recipe1);

                // Recipe 2: Avocado Toast Supreme
                Recipe recipe2 = new Recipe();
                recipe2.setUser(demoUser);
                recipe2.setName("Avocado Toast Supreme");
                recipe2.setDescription("Crispy sourdough toast topped with creamy mashed avocado, poached egg, and chili flakes.");
                recipe2.setCategory("Breakfast");
                recipe2.setTimeMinutes(10);
                recipe2.setDifficulty("Easy");
                recipe2.setServings(1);
                recipe2.setImageUrl("https://images.unsplash.com/photo-1525351484163-7529414344d8?q=80&w=800&auto=format&fit=crop");
                recipe2.setRating(4.6);
                recipe2.setReviewCount(89);
                recipe2.setNotes("Squeeze a lime fresh over the mashed avocado to prevent browning and add bright acidity.");

                recipe2.addIngredient(new Ingredient("Sourdough Bread", 2.0, "slices"));
                recipe2.addIngredient(new Ingredient("Ripe Avocado", 1.0, "pc"));
                recipe2.addIngredient(new Ingredient("Eggs", 2.0, "pcs"));
                recipe2.addIngredient(new Ingredient("Chili Flakes", 0.5, "tsp"));
                recipe2.addIngredient(new Ingredient("Lime Juice", 1.0, "tsp"));

                recipe2.addInstruction(new Instruction(1, "Toast Bread", "Toast sourdough slices in a toaster or skillet until golden brown and crispy."));
                recipe2.addInstruction(new Instruction(2, "Prepare Avocado", "Mash ripe avocado in a bowl with lime juice, salt, and pepper until creamy yet slightly chunky."));
                recipe2.addInstruction(new Instruction(3, "Cook Eggs", "Poach or fry eggs to your preferred doneness with runny yolks."));
                recipe2.addInstruction(new Instruction(4, "Assemble", "Spread avocado generously over toast, top with egg, and sprinkle with red chili flakes."));

                recipe2.setNutrition(new Nutrition(380, 16.0, 32.0, 22.0));
                recipeRepository.save(recipe2);

                // Recipe 3: Berry Smoothie Bowl
                Recipe recipe3 = new Recipe();
                recipe3.setUser(demoUser);
                recipe3.setName("Berry Smoothie Bowl");
                recipe3.setDescription("A thick and refreshing berry blend topped with chia seeds, fresh fruit, and crunchy granola.");
                recipe3.setCategory("Snacks");
                recipe3.setTimeMinutes(10);
                recipe3.setDifficulty("Easy");
                recipe3.setServings(1);
                recipe3.setImageUrl("https://images.unsplash.com/photo-1590301157890-4810ed352733?q=80&w=800&auto=format&fit=crop");
                recipe3.setRating(4.9);
                recipe3.setReviewCount(142);
                recipe3.setNotes("Use frozen berries and minimal liquid for the thickest smoothie bowl texture.");

                recipe3.addIngredient(new Ingredient("Frozen Mixed Berries", 150.0, "g"));
                recipe3.addIngredient(new Ingredient("Banana", 1.0, "pc"));
                recipe3.addIngredient(new Ingredient("Almond Milk", 100.0, "ml"));
                recipe3.addIngredient(new Ingredient("Granola", 30.0, "g"));
                recipe3.addIngredient(new Ingredient("Chia Seeds", 1.0, "tbsp"));

                recipe3.addInstruction(new Instruction(1, "Blend Ingredients", "Combine frozen berries, banana, and almond milk in a high-speed blender until thick and creamy."));
                recipe3.addInstruction(new Instruction(2, "Pour & Decorate", "Pour into a bowl and arrange granola, fresh berries, chia seeds, and coconut flakes on top."));

                recipe3.setNutrition(new Nutrition(310, 8.0, 54.0, 7.0));
                recipeRepository.save(recipe3);
            }
        };
    }
}
