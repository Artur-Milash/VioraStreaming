import {configureStore} from "@reduxjs/toolkit";
import authReducer from "./auth.ts";
import modalsReducer from "./modals.ts";
import filtersReducer from "./filterSlice.ts";
import toastReducer from "./toast.ts";

export const store = configureStore({
  reducer: {
    auth: authReducer,
    modal: modalsReducer,
    filters: filtersReducer,
    toast: toastReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;