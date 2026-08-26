# Google Cloud Platform (GCP) Integration & Deployment Guide

This guide details how to deploy the **Yummiee** application from the `integration` branch directly to Google Cloud Platform (GCP) using **GCP Cloud Run** or **GCP App Engine**.

---

## 🚀 Option 1: Deploying to GCP Cloud Run (Recommended)

GCP Cloud Run automatically builds Docker containers and provides instant HTTPS URLs with auto-scaling.

### Step 1: Install & Initialize Google Cloud SDK
```bash
gcloud auth login
gcloud config set project YOUR_GCP_PROJECT_ID
```

### Step 2: Deploy Spring Boot Backend to Cloud Run
```bash
cd backend-java
gcloud run deploy yummiee-backend-java \
  --source . \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --port 8080
```
*Note the returned service URL (e.g., `https://yummiee-backend-java-xyz-uc.a.run.app`).*

### Step 3: Deploy Node.js Backend (Alternative Option)
```bash
cd Backend
gcloud run deploy yummiee-backend-node \
  --source . \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --port 8080
```

### Step 4: Deploy React Frontend to Cloud Run
Pass your backend Cloud Run URL into frontend environment variables:
```bash
cd Frontend
gcloud run deploy yummiee-frontend \
  --source . \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --port 80 \
  --set-env-vars VITE_API_BASE_URL="https://yummiee-backend-java-xyz-uc.a.run.app/api"
```

---

## ⚡ Option 2: Continuous Deployment (CD) via GCP Cloud Build

You can connect your GitHub repository's `integration` branch directly to GCP Cloud Build:
1. Go to **GCP Console -> Cloud Build -> Triggers**.
2. Click **Create Trigger**.
3. Select repository `lingeswaran05/Yummy` and set Target Branch to `integration`.
4. Select Build Configuration: `Dockerfile` or `cloudbuild.yaml`.
5. Whenever changes are pushed to `integration`, GCP automatically builds and redeploys your app!
