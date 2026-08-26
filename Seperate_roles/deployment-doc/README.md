# Role: Deployment & DevOps Engineering (`deployment-doc`)

## 📌 Scope & Responsibilities
This document provides step-by-step deployment instructions for hosting the **React Frontend on Vercel** and the **Spring Boot Java Backend on Render / Railway / Docker**.

---

## 🚀 1. Deploying React Frontend to Vercel

### Step 1: Push Repository to GitHub
Ensure the project is pushed to [https://github.com/lingeswaran05/Yummy](https://github.com/lingeswaran05/Yummy).

### Step 2: Import Project in Vercel Dashboard
1. Go to [Vercel Dashboard](https://vercel.com/dashboard) and click **"Add New" -> "Project"**.
2. Import the `lingeswaran05/Yummy` GitHub repository.
3. Configure Root Directory: Select `Frontend/`.

### Step 3: Configure Environment Variables
Add the following in Vercel Environment Variables settings:
```env
VITE_API_BASE_URL=https://yummiee-backend.onrender.com/api
VITE_CLERK_PUBLISHABLE_KEY=pk_test_bWVhc3VyZWQtaG9uZXliZWUtNzE1OS5jbGVyay5hY2NvdW50cy5kZXYk
```

### Step 4: Deploy
Click **Deploy**. Vercel will build the SPA assets via `vite build` and output static assets to `dist/`.

---

## 🐳 2. Deploying Spring Boot Backend (Docker / Render / Railway)

### Dockerfile for Spring Boot (`backend-java/Dockerfile`):
```dockerfile
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/yummiee-backend-java-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## 🌿 Git Branching Instructions
```bash
git checkout -b doc/deployment-devops
git add README.md
git commit -m "docs(devops): add Vercel frontend deployment and Docker container guide"
git push -u origin doc/deployment-devops
```
