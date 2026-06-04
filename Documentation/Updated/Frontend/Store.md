# Redux Store Architecture Documentation

## Overview

This document describes the Redux store structure of the application, including slices for **authentication**, **filters**, **modals**, and the global store configuration.

The state management is built using **Redux Toolkit (@reduxjs/toolkit)**.

---

## Store Structure

```
store/
├── auth.ts
├── filterSlice.ts
├── modals.ts
├── store.ts
```

---

## Global Store Configuration

### store.ts

The main Redux store combines all slices into a single state tree.

```ts
import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./auth";
import modalsReducer from "./modals";
import filtersReducer from "./filterSlice";

export const store = configureStore({
  reducer: {
    auth: authReducer,
    modal: modalsReducer,
    filters: filtersReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
```

---

## Auth Slice

### Purpose

Manages authentication state using JWT token.

### State

```ts
interface AuthState {
  token: string | null;
}
```

### Initial State

* Token is loaded from `localStorage` via `getToken()`

---

### Reducers

#### setCredentials

Stores JWT token after login:

* updates Redux state
* saves token to `localStorage`
* saves token expiry via `saveTokenExpiry()`

```ts
setCredentials(state, action: PayloadAction<{ token: string }>)
```

#### clearCredentials

Clears authentication state:

* removes token from Redux
* removes token from storage via `removeToken()`

---

### Implementation

```ts
import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import { getToken, removeToken, saveTokenExpiry } from "../utils/apiUtils";

interface AuthState {
  token: string | null;
}

const initialState: AuthState = {
  token: getToken(),
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setCredentials(state, action: PayloadAction<{ token: string }>) {
      state.token = action.payload.token;
      localStorage.setItem("JWT_TOKEN", action.payload.token);
      saveTokenExpiry();
    },
    clearCredentials(state) {
      state.token = null;
      removeToken();
    },
  },
});

export const { setCredentials, clearCredentials } = authSlice.actions;
export default authSlice.reducer;
```

---

## Filters Slice

### Purpose

Stores UI filtering state for content (e.g. movies search/filtering).

---

### State

```ts
export interface FiltersState {
  genres: number[];
  rating: number;
  releaseYear: number[];
  duration: string;
  title: string;
}
```

---

### Initial State

Uses constants:

* `DEFAULT_RATING`
* `DEFAULT_RELEASE_YEAR`
* `ANY_DURATION`

---

### Reducers

| Action         | Description                    |
| -------------- | ------------------------------ |
| setGenres      | Updates selected genres        |
| setRating      | Updates rating filter          |
| setReleaseYear | Updates year range             |
| setDuration    | Updates duration filter        |
| setTitle       | Updates search title           |
| resetFilters   | Resets state to initial values |

---

### Implementation

```ts
import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import {
  ANY_DURATION,
  DEFAULT_RATING,
  DEFAULT_RELEASE_YEAR,
} from "../constants/filterConstants";

export interface FiltersState {
  genres: number[];
  rating: number;
  releaseYear: number[];
  duration: string;
  title: string;
}

const initialState: FiltersState = {
  genres: [],
  rating: DEFAULT_RATING,
  releaseYear: DEFAULT_RELEASE_YEAR,
  duration: ANY_DURATION,
  title: "",
};

const filtersSlice = createSlice({
  name: "filters",
  initialState,
  reducers: {
    setGenres(state, action: PayloadAction<number[]>) {
      state.genres = action.payload;
    },
    setRating(state, action: PayloadAction<number>) {
      state.rating = action.payload;
    },
    setReleaseYear(state, action: PayloadAction<number[]>) {
      state.releaseYear = action.payload;
    },
    setDuration(state, action: PayloadAction<string>) {
      state.duration = action.payload;
    },
    setTitle(state, action: PayloadAction<string>) {
      state.title = action.payload;
    },
    resetFilters() {
      return initialState;
    },
  },
});

export const {
  setGenres,
  setRating,
  setReleaseYear,
  setDuration,
  setTitle,
  resetFilters,
} = filtersSlice.actions;

export default filtersSlice.reducer;
```

---

## Modals Slice

### Purpose

Manages modal stack (supports multiple modals simultaneously).

---

### State

```ts
interface ModalsState {
  stack: Modal[];
}

type Modal = {
  data: unknown;
  id: string;
  type: ModalTypes;
};
```

---

### Behavior

This slice uses a **stack-based modal system**:

* `openModal` → pushes modal to stack
* `closeModal` → pops last modal

---

### Selectors

| Selector        | Description              |
| --------------- | ------------------------ |
| getModalsStack  | returns full modal stack |
| getCurrentModal | returns top modal        |

---

### Implementation

```ts
import type { ModalTypes } from "../types/modalTypes";
import { createSlice, type PayloadAction } from "@reduxjs/toolkit";

type Modal = {
  data: unknown;
  id: string;
  type: ModalTypes;
};

interface ModalsState {
  stack: Modal[];
}

const modalsSlice = createSlice({
  name: "modalsSlice",
  initialState: {
    stack: [],
  } as ModalsState,
  reducers: {
    openModal(state, action: PayloadAction<Modal>) {
      state.stack.push(action.payload);
    },
    closeModal(state) {
      state.stack.pop();
    },
  },
});

export const getModalsStack = (state: { modal: ModalsState }) =>
  state.modal.stack;

export const getCurrentModal = (state: { modal: ModalsState }) =>
  state.modal.stack[state.modal.stack.length - 1] || null;

export const ModalsSelectors = {
  getModalsStack,
  getCurrentModal,
};

export const { openModal, closeModal } = modalsSlice.actions;
export default modalsSlice.reducer;
```

---

## State Shape Overview

```ts
RootState = {
  auth: {
    token: string | null
  },
  modal: {
    stack: Modal[]
  },
  filters: {
    genres: number[]
    rating: number
    releaseYear: number[]
    duration: string
    title: string
  }
}
```

---

## Summary

The Redux architecture provides:

* Centralized global state management
* Scalable slice-based structure
* Persistent authentication handling
* Flexible UI state (filters + modals)
* Strong TypeScript typing across store
