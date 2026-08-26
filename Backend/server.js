import express from "express";
import cors from "cors";
import sqlite3 from "sqlite3";
import dotenv from "dotenv";
import { clerkMiddleware, getAuth } from "@clerk/express";
import path from "path";
import { fileURLToPath } from "url";

dotenv.config();

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
const PORT = process.env.PORT || 8080;

app.use(cors({
  origin: "*",
  credentials: true
}));
app.use(express.json());

// Initialize Clerk Middleware
app.use(clerkMiddleware());

// SQLite Database Setup
const dbPath = path.join(__dirname, "yummiee.db");
const db = new sqlite3.Database(dbPath, (err) => {
  if (err) {
    console.error("Failed to connect to SQLite database:", err.message);
  } else {
    console.log("Connected to SQLite database at:", dbPath);
  }
});

// Helper functions for Async SQLite queries
function dbGet(sql, params = []) {
  return new Promise((resolve, reject) => {
    db.get(sql, params, (err, row) => {
      if (err) reject(err);
      else resolve(row);
    });
  });
}

function dbAll(sql, params = []) {
  return new Promise((resolve, reject) => {
    db.all(sql, params, (err, rows) => {
      if (err) reject(err);
      else resolve(rows);
    });
  });
}

function dbRun(sql, params = []) {
  return new Promise((resolve, reject) => {
    db.run(sql, params, function (err) {
      if (err) reject(err);
      else resolve({ lastID: this.lastID, changes: this.changes });
    });
  });
}

// User helper - syncs Clerk user with SQLite users table
async function getOrCreateUserId(req) {
  let clerkUserId = null;
  try {
    const auth = getAuth(req);
    if (auth && auth.userId) {
      clerkUserId = auth.userId;
    }
  } catch (err) {
    // Auth fallback
  }

  if (!clerkUserId) {
    clerkUserId = req.headers["x-clerk-user-id"] || "mock_clerk_user_1";
  }

  let user = await dbGet("SELECT id FROM users WHERE clerk_user_id = ?", [clerkUserId]);
  if (!user) {
    const res = await dbRun(
      "INSERT INTO users (clerk_user_id, email, first_name, last_name, created_at, updated_at) VALUES (?, ?, ?, ?, datetime('now'), datetime('now'))",
      [clerkUserId, `${clerkUserId}@yummiee.com`, "User", ""]
    );
    return res.lastID;
  }
  return user.id;
}

// Helper to format full recipe details
async function fetchFullRecipe(recipeId) {
  const recipe = await dbGet("SELECT * FROM recipes WHERE id = ?", [recipeId]);
  if (!recipe) return null;

  const ingredients = await dbAll(
    "SELECT id, name, quantity, unit FROM ingredients WHERE recipe_id = ? ORDER BY id ASC",
    [recipeId]
  );
  const instructions = await dbAll(
    "SELECT id, step_number, title, description FROM instructions WHERE recipe_id = ? ORDER BY step_number ASC",
    [recipeId]
  );
  const nutrition = await dbGet(
    "SELECT calories, protein, carbs, fat FROM nutrition WHERE recipe_id = ?",
    [recipeId]
  );

  return {
    id: recipe.id,
    name: recipe.name,
    description: recipe.description,
    category: recipe.category,
    time: recipe.time_minutes,
    difficulty: recipe.difficulty,
    servings: recipe.servings,
    image: recipe.image_url,
    rating: recipe.rating || 4.5,
    reviews: recipe.review_count || 0,
    notes: recipe.notes || "",
    ingredients: ingredients.map((ing) => ({
      id: ing.id,
      name: ing.name,
      quantity: ing.quantity,
      unit: ing.unit,
    })),
    instructions: instructions.map((inst) => ({
      step: inst.step_number,
      title: inst.title || `Step ${inst.step_number}`,
      description: inst.description,
    })),
    nutrition: nutrition
      ? {
          calories: nutrition.calories,
          protein: nutrition.protein,
          carbs: nutrition.carbs,
          fat: nutrition.fat,
        }
      : null,
  };
}

// ================= RECIPE API ENDPOINTS =================

// GET /api/recipes
app.get("/api/recipes", async (req, res) => {
  try {
    const { search, category, difficulty, sort } = req.query;

    let sql = "SELECT * FROM recipes WHERE 1=1";
    const params = [];

    if (search) {
      sql += " AND (name LIKE ? OR description LIKE ?)";
      params.push(`%${search}%`, `%${search}%`);
    }

    if (category && category !== "All") {
      sql += " AND category = ?";
      params.push(category);
    }

    if (difficulty) {
      sql += " AND difficulty = ?";
      params.push(difficulty);
    }

    if (sort === "Quickest") {
      sql += " ORDER BY time_minutes ASC";
    } else if (sort === "Most Liked") {
      sql += " ORDER BY rating DESC";
    } else {
      sql += " ORDER BY id DESC";
    }

    const recipes = await dbAll(sql, params);
    const fullRecipes = await Promise.all(
      recipes.map((recipe) => fetchFullRecipe(recipe.id))
    );

    res.json(fullRecipes);
  } catch (err) {
    console.error("Error fetching recipes:", err);
    res.status(500).json({ message: "Failed to fetch recipes" });
  }
});

// GET /api/recipes/:id
app.get("/api/recipes/:id", async (req, res) => {
  try {
    const recipe = await fetchFullRecipe(req.params.id);
    if (!recipe) {
      return res.status(404).json({ message: "Recipe not found" });
    }
    res.json(recipe);
  } catch (err) {
    console.error("Error fetching recipe by id:", err);
    res.status(500).json({ message: "Failed to fetch recipe" });
  }
});

// POST /api/recipes
app.post("/api/recipes", async (req, res) => {
  try {
    const userId = await getOrCreateUserId(req);
    const {
      name,
      description,
      category,
      time,
      difficulty,
      servings,
      image,
      ingredients = [],
      instructions = [],
      nutrition,
      notes,
    } = req.body;

    const result = await dbRun(
      `INSERT INTO recipes (user_id, name, description, category, time_minutes, difficulty, servings, image_url, rating, review_count, notes, created_at, updated_at)
       VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, datetime('now'), datetime('now'))`,
      [
        userId,
        name,
        description,
        category || "Dinner",
        time || 30,
        difficulty || "Easy",
        servings || 2,
        image || "https://images.unsplash.com/photo-1495521821757-a1efb6729352?w=800",
        4.5,
        1,
        notes || "",
      ]
    );

    const recipeId = result.lastID;

    // Insert ingredients
    for (const ing of ingredients) {
      await dbRun(
        "INSERT INTO ingredients (recipe_id, name, quantity, unit) VALUES (?, ?, ?, ?)",
        [recipeId, ing.name, ing.quantity || 1, ing.unit || "unit"]
      );
    }

    // Insert instructions
    for (let i = 0; i < instructions.length; i++) {
      const inst = instructions[i];
      await dbRun(
        "INSERT INTO instructions (recipe_id, step_number, title, description) VALUES (?, ?, ?, ?)",
        [recipeId, inst.step || i + 1, inst.title || `Step ${i + 1}`, inst.description || inst.text || ""]
      );
    }

    // Insert nutrition if provided
    if (nutrition) {
      await dbRun(
        "INSERT INTO nutrition (recipe_id, calories, protein, carbs, fat) VALUES (?, ?, ?, ?, ?)",
        [recipeId, nutrition.calories || 300, nutrition.protein || 10, nutrition.carbs || 30, nutrition.fat || 10]
      );
    }

    const createdRecipe = await fetchFullRecipe(recipeId);
    res.status(201).json(createdRecipe);
  } catch (err) {
    console.error("Error creating recipe:", err);
    res.status(500).json({ message: "Failed to create recipe" });
  }
});

// PUT /api/recipes/:id
app.put("/api/recipes/:id", async (req, res) => {
  try {
    const recipeId = req.params.id;
    const { name, description, category, time, difficulty, servings, image, notes } = req.body;

    await dbRun(
      `UPDATE recipes SET name = ?, description = ?, category = ?, time_minutes = ?, difficulty = ?, servings = ?, image_url = ?, notes = ?, updated_at = datetime('now')
       WHERE id = ?`,
      [name, description, category, time, difficulty, servings, image, notes, recipeId]
    );

    const updated = await fetchFullRecipe(recipeId);
    res.json(updated);
  } catch (err) {
    console.error("Error updating recipe:", err);
    res.status(500).json({ message: "Failed to update recipe" });
  }
});

// DELETE /api/recipes/:id
app.delete("/api/recipes/:id", async (req, res) => {
  try {
    const recipeId = req.params.id;
    await dbRun("DELETE FROM ingredients WHERE recipe_id = ?", [recipeId]);
    await dbRun("DELETE FROM instructions WHERE recipe_id = ?", [recipeId]);
    await dbRun("DELETE FROM nutrition WHERE recipe_id = ?", [recipeId]);
    await dbRun("DELETE FROM wishlist WHERE recipe_id = ?", [recipeId]);
    await dbRun("DELETE FROM recipes WHERE id = ?", [recipeId]);

    res.status(204).send();
  } catch (err) {
    console.error("Error deleting recipe:", err);
    res.status(500).json({ message: "Failed to delete recipe" });
  }
});

// ================= WISHLIST API ENDPOINTS =================

// GET /api/wishlist
app.get("/api/wishlist", async (req, res) => {
  try {
    const userId = await getOrCreateUserId(req);
    const wishlistItems = await dbAll(
      "SELECT recipe_id FROM wishlist WHERE user_id = ?",
      [userId]
    );

    const fullRecipes = await Promise.all(
      wishlistItems.map((item) => fetchFullRecipe(item.recipe_id))
    );

    res.json(fullRecipes.filter(Boolean));
  } catch (err) {
    console.error("Error fetching wishlist:", err);
    res.status(500).json({ message: "Failed to fetch wishlist" });
  }
});

// POST /api/wishlist/:recipeId
app.post("/api/wishlist/:recipeId", async (req, res) => {
  try {
    const userId = await getOrCreateUserId(req);
    const recipeId = req.params.recipeId;

    const existing = await dbGet(
      "SELECT id FROM wishlist WHERE user_id = ? AND recipe_id = ?",
      [userId, recipeId]
    );

    if (!existing) {
      await dbRun(
        "INSERT INTO wishlist (user_id, recipe_id, created_at) VALUES (?, ?, datetime('now'))",
        [userId, recipeId]
      );
    }

    res.status(201).json({ message: "Added to wishlist" });
  } catch (err) {
    console.error("Error adding to wishlist:", err);
    res.status(500).json({ message: "Failed to add to wishlist" });
  }
});

// DELETE /api/wishlist/:recipeId
app.delete("/api/wishlist/:recipeId", async (req, res) => {
  try {
    const userId = await getOrCreateUserId(req);
    const recipeId = req.params.recipeId;

    await dbRun(
      "DELETE FROM wishlist WHERE user_id = ? AND recipe_id = ?",
      [userId, recipeId]
    );

    res.status(204).send();
  } catch (err) {
    console.error("Error removing from wishlist:", err);
    res.status(500).json({ message: "Failed to remove from wishlist" });
  }
});

// ================= SHOPPING LIST API ENDPOINTS =================

// GET /api/shopping-list
app.get("/api/shopping-list", async (req, res) => {
  try {
    const userId = await getOrCreateUserId(req);
    const items = await dbAll(
      `SELECT s.id, s.name, s.quantity, s.unit, s.checked, s.recipe_id, r.name as recipe_name
       FROM shopping_list_items s
       LEFT JOIN recipes r ON s.recipe_id = r.id
       WHERE s.user_id = ?
       ORDER BY s.id DESC`,
      [userId]
    );

    const formatted = items.map((item) => ({
      id: item.id,
      name: item.name,
      quantity: item.quantity,
      unit: item.unit,
      checked: Boolean(item.checked),
      recipeId: item.recipe_id,
      recipeName: item.recipe_name || "",
    }));

    res.json(formatted);
  } catch (err) {
    console.error("Error fetching shopping list:", err);
    res.status(500).json({ message: "Failed to fetch shopping list" });
  }
});

// POST /api/shopping-list
app.post("/api/shopping-list", async (req, res) => {
  try {
    const userId = await getOrCreateUserId(req);
    const { name, quantity, unit, recipeId } = req.body;

    const result = await dbRun(
      `INSERT INTO shopping_list_items (user_id, recipe_id, name, quantity, unit, checked, created_at)
       VALUES (?, ?, ?, ?, ?, 0, datetime('now'))`,
      [userId, recipeId || null, name, quantity || 1, unit || "unit"]
    );

    res.status(201).json({
      id: result.lastID,
      name,
      quantity: quantity || 1,
      unit: unit || "unit",
      checked: false,
      recipeId: recipeId || null,
    });
  } catch (err) {
    console.error("Error adding shopping list item:", err);
    res.status(500).json({ message: "Failed to add shopping list item" });
  }
});

// PUT /api/shopping-list/:id
app.put("/api/shopping-list/:id", async (req, res) => {
  try {
    const userId = await getOrCreateUserId(req);
    const itemId = req.params.id;
    const { checked, quantity, name } = req.body;

    if (checked !== undefined) {
      await dbRun(
        "UPDATE shopping_list_items SET checked = ? WHERE id = ? AND user_id = ?",
        [checked ? 1 : 0, itemId, userId]
      );
    }

    if (quantity !== undefined) {
      await dbRun(
        "UPDATE shopping_list_items SET quantity = ? WHERE id = ? AND user_id = ?",
        [quantity, itemId, userId]
      );
    }

    if (name !== undefined) {
      await dbRun(
        "UPDATE shopping_list_items SET name = ? WHERE id = ? AND user_id = ?",
        [name, itemId, userId]
      );
    }

    res.json({ message: "Shopping list item updated" });
  } catch (err) {
    console.error("Error updating shopping list item:", err);
    res.status(500).json({ message: "Failed to update shopping list item" });
  }
});

// DELETE /api/shopping-list/:id
app.delete("/api/shopping-list/:id", async (req, res) => {
  try {
    const userId = await getOrCreateUserId(req);
    const itemId = req.params.id;

    await dbRun(
      "DELETE FROM shopping_list_items WHERE id = ? AND user_id = ?",
      [itemId, userId]
    );

    res.status(204).send();
  } catch (err) {
    console.error("Error deleting shopping list item:", err);
    res.status(500).json({ message: "Failed to delete shopping list item" });
  }
});

// DELETE /api/shopping-list
app.delete("/api/shopping-list", async (req, res) => {
  try {
    const userId = await getOrCreateUserId(req);
    await dbRun("DELETE FROM shopping_list_items WHERE user_id = ?", [userId]);
    res.status(204).send();
  } catch (err) {
    console.error("Error clearing shopping list:", err);
    res.status(500).json({ message: "Failed to clear shopping list" });
  }
});

// Start Server
app.listen(PORT, () => {
  console.log(`Backend REST API server running at http://localhost:${PORT}`);
});
