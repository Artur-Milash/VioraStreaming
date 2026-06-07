import {Alert, Snackbar} from "@mui/material";
import {useDispatch, useSelector} from "react-redux";
import type {AppDispatch} from "../../store/store.ts";
import {getToastState, hideToast} from "../../store/toast.ts";

export function Toast() {
  const dispatch = useDispatch<AppDispatch>();
  const {open, message, severity} = useSelector(getToastState);

  return (
      <Snackbar
          open={open}
          autoHideDuration={3000}
          onClose={() => dispatch(hideToast())}
          anchorOrigin={{vertical: "bottom", horizontal: "center"}}
      >
        <Alert severity={severity} onClose={() => dispatch(hideToast())} variant="filled">
          {message}
        </Alert>
      </Snackbar>
  );
}
