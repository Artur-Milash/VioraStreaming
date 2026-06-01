import {createSlice, type PayloadAction} from "@reduxjs/toolkit";

type ToastSeverity = "success" | "error" | "info" | "warning";

interface ToastState {
  open: boolean;
  message: string;
  severity: ToastSeverity;
}

const toastSlice = createSlice({
  name: "toast",
  initialState: {
    open: false,
    message: "",
    severity: "success",
  } as ToastState,
  reducers: {
    showToast(state, action: PayloadAction<{message: string; severity?: ToastSeverity}>) {
      state.open = true;
      state.message = action.payload.message;
      state.severity = action.payload.severity ?? "success";
    },
    hideToast(state) {
      state.open = false;
    },
  },
});

export const {showToast, hideToast} = toastSlice.actions;
export const getToastState = (state: {toast: ToastState}) => state.toast;
export default toastSlice.reducer;
