import {createSlice, type PayloadAction} from "@reduxjs/toolkit";
import {getToken, removeToken, saveTokenExpiry} from "../utils/apiUtils.ts";

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
    setCredentials(
        state,
        action: PayloadAction<{ token: string; }>
    ) {
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

export const {setCredentials, clearCredentials} = authSlice.actions;
export default authSlice.reducer;