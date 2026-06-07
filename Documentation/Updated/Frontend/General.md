# Frontend Architecture & Routing Documentation

## Overview

This document describes the structure of the frontend application, including routing guards, route constants, error handling configuration, and overall project organization.

The application is built using **React + TypeScript** with **react-router-dom** for routing.

---

## Project Structure

### src/
* api/
* assets/
* components/
* constants/
* contexts/
* hooks/
* models/
* pages/
* routes/
* store/
* utils/
* App.tsx
* main.tsx

### Folder Responsibilities

- **api/** – API clients and request handlers
- **assets/** – Static assets (images, icons, etc.)
- **components/** – Reusable UI components
- **constants/** – Application-wide constants (routes, error messages, etc.)
- **contexts/** – React contexts (auth, theme, etc.)
- **hooks/** – Custom React hooks
- **models/** – TypeScript models and interfaces
- **pages/** – Route-level pages
- **routes/** – Route definitions and guards
- **store/** – State management (Redux/Zustand/etc.)
- **utils/** – Utility functions and helpers

---

## Route Constants

### `API_PAGE`

Defines main application routes:

```ts
export const API_PAGE = {
  Auth: '/auth',
  Home: '/home',
  Movies: '/movies',
  History: "/history",
  Assistant: "/assistant",
  Settings: "/settings"
} as const;
```

---

### PAGE_ROUTES

```
export const PAGE_ROUTES = {
  Register: "register",
  ForgotPassword: "forgot-password",
  DropPassword: "drop-password"
};
```

---

## Route Guards

## ProtectedRoute

Purpose:
Restricts access to authenticated users only.

Logic:
- getToken()
- no token → redirect /auth
- token exists → render children or layout

```
import { Navigate } from "react-router-dom";
import { API_PAGE } from "../constants/routingConstants";
import { AppLayout } from "../components/Layout/AppLayout";
import { getToken } from "../utils/apiUtils";
import type { ReactNode } from "react";

interface ProtectedRouteProps {
  children?: ReactNode;
}

const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
  const token = getToken();

  if (!token) {
    return <Navigate to={API_PAGE.Auth} replace />;
  }

  return children ? <>{children}</> : <AppLayout />;
};

export default ProtectedRoute;
```

---

## AnonRoute

Purpose:
Blocks authenticated users from auth pages.

```
import { API_PAGE } from "../constants/routingConstants";
import { Navigate, Outlet } from "react-router-dom";

export const AnonRoute = () => {
  const token = localStorage.getItem("JWT_TOKEN");

  if (token) {
    return <Navigate to={API_PAGE.Home} replace />;
  }

  return <Outlet />;
};
```

---

## Error Handling

```
export const ERROR_PAGE_CONSTANTS = {
  BACK_TO_HOME_BUTTON: "Back to Home",
  FALLBACK_TITLE: "Something Went Wrong",
  FALLBACK_DESCRIPTION: "An unexpected error occurred."
} as const;

export const ERROR_MESSAGES = {
  404: {
    title: "Page Not Found",
    description: "The page doesn't exist."
  },
  401: {
    title: "Unauthorized",
    description: "No permission."
  },
  403: {
    title: "Forbidden",
    description: "Access denied."
  },
  500: {
    title: "Server Error",
    description: "Try again later."
  }
};
```

---

## Routing Flow

User request
→ check token
→ redirect /auth or /home

---

## Summary

- JWT-based auth
- Route guards
- Centralized constants
- Layout-based routing
