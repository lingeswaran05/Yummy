# Role: System Integration & REST API Contract Specification (`integration-doc`)

## 📌 Scope & Responsibilities
This document provides full technical specifications for how the React Frontend communicates with the Spring Boot (or Express) Backend, detailing REST endpoint URLs, HTTP methods, headers, request bodies, and response payloads.

---

## 🔌 API Contract Reference

### 1. Base URL
`http://localhost:8080/api`

### 2. Standard Request Headers
```http
Content-Type: application/json
Authorization: Bearer <clerk_session_token>
x-clerk-user-id: <user_id>
```

---

## 📡 Endpoints Summary

### Recipe APIs
| Method | Endpoint | Description | Query Parameters |
| :--- | :--- | :--- | :--- |
| `GET` | `/recipes` | Get all recipes | `search`, `category`, `difficulty`, `sort` |
| `GET` | `/recipes/:id` | Get recipe by ID | - |
| `POST` | `/recipes` | Create new recipe | - |
| `PUT` | `/recipes/:id` | Update existing recipe | - |
| `DELETE` | `/recipes/:id` | Delete recipe | - |

#### Sample Create Recipe Payload (`POST /api/recipes`):
```json
{
  "name": "Tuscan Chicken",
  "description": "Delicious creamy chicken with sun-dried tomatoes",
  "category": "Dinner",
  "time": 35,
  "difficulty": "Easy",
  "servings": 4,
  "image": "https://images.unsplash.com/photo-1604908176997-125f25cc6f3d",
  "notes": "Serve warm with pasta",
  "ingredients": [
    { "name": "Chicken Breasts", "quantity": 2, "unit": "large" },
    { "name": "Heavy Cream", "quantity": 1, "unit": "cup" }
  ],
  "instructions": [
    { "step": 1, "title": "Sear Chicken", "description": "Sear chicken breasts in olive oil." },
    { "step": 2, "title": "Make Sauce", "description": "Add cream and tomatoes." }
  ],
  "nutrition": {
    "calories": 480,
    "protein": 38,
    "carbs": 12,
    "fat": 32
  }
}
```

---

### Wishlist APIs
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/wishlist` | Get user's saved wishlist recipes |
| `POST` | `/wishlist/:recipeId` | Add recipe to wishlist |
| `DELETE` | `/wishlist/:recipeId` | Remove recipe from wishlist |

---

### Shopping List APIs
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/shopping-list` | Get active user's shopping items |
| `POST` | `/shopping-list` | Add item to shopping list |
| `PUT` | `/shopping-list/:id` | Update item (toggle `checked`, modify `quantity`) |
| `DELETE` | `/shopping-list/:id` | Delete single item |
| `DELETE` | `/shopping-list` | Clear user's entire shopping list |

---

## 🌿 Git Branching Instructions
```bash
git checkout -b doc/integration-spec
git add README.md
git commit -m "docs(api): add REST API contract specs and integration guide"
git push -u origin doc/integration-spec
```
