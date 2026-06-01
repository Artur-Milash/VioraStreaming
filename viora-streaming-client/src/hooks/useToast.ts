import {useDispatch} from "react-redux";
import type {AppDispatch} from "../store/store.ts";
import {showToast} from "../store/toast.ts";

type ToastSeverity = "success" | "error" | "info" | "warning";

export function useToast() {
  const dispatch = useDispatch<AppDispatch>();

  return (message: string, severity: ToastSeverity = "success") => {
    dispatch(showToast({message, severity}));
  };
}
