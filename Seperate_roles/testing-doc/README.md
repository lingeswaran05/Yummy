# Role: Automated & Manual Quality Assurance Testing (`testing-doc`)

## 📌 Scope & Responsibilities
This document provides guidelines, unit test suites, integration test suites, and manual QA checklists for verifying both the Frontend React UI and the Spring Boot Backend.

---

## 🧪 Testing Suites & Instructions

### 1. Spring Boot Backend Unit & Integration Testing
Run Maven unit test suite:
```bash
cd backend-java
.\mvnw.cmd test
```

#### Sample Spring Boot MockMVC Test (`RecipeControllerTest.java`):
```java
@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetRecipesReturnsOk() throws Exception {
        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
```

---

### 2. Frontend Component Testing
Run Vitest or React Testing Library suites:
```bash
cd Frontend
npm test
```

---

### 3. API Integration Testing Checklist
- [ ] **Recipe Search**: Verify searching for `"chicken"` filters catalog correctly.
- [ ] **Recipe Creation**: Verify creating a recipe inserts ingredients, steps, and nutrition records into database.
- [ ] **Wishlist Toggle**: Verify clicking bookmark adds/removes item from wishlist without duplicate SQL errors.
- [ ] **Shopping List Toggling**: Verify checking off an item updates `checked = 1` in SQL database.

---

## 🌿 Git Branching Instructions
```bash
git checkout -b doc/testing-qa
git add README.md
git commit -m "docs(test): add Spring Boot unit test examples and API QA checklist"
git push -u origin doc/testing-qa
```
