import type {ModalProps} from "../../../types/modalTypes.ts";
import {Box, DialogActions, DialogContent, Typography} from "@mui/material";
import {Modal} from "../Modal/Modal.tsx";
import {VioraButton} from "../../Button/VioraButton.tsx";
import ReportGmailerrorredIcon from '@mui/icons-material/ReportGmailerrorred';
import {AUTH_CONSTANTS} from "../../../constants/authConstants.ts";

export function NetworkErrorModal({data, onClose}: ModalProps) {
  const modalData = data as {
    message: string;
  }

  return (
      <Modal onClose={onClose}>

        <DialogContent sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          flexDirection: "column",
          gap: "12px",
        }}>
          <Box sx={{
            height: "64px",
            width: "64px",
            backgroundColor: "#2A2A2C",
            borderRadius: "12px",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
          }}>
            <ReportGmailerrorredIcon
                fontSize="large"
                color="error"
            />
          </Box>

          <Typography variant="h5" color={"text.primary"} sx={{
            fontWeight: "bold"
          }}>
            {AUTH_CONSTANTS.NETWORK_ERROR_TITLE}
          </Typography>

          <Typography variant="body1" color={"text.primary"} align="center">
            {modalData.message}
          </Typography>

        </DialogContent>

        <DialogActions>
          <VioraButton onClick={onClose} name={AUTH_CONSTANTS.CLOSE_BUTTON} type={"button"}/>
        </DialogActions>
      </Modal>
  )

}