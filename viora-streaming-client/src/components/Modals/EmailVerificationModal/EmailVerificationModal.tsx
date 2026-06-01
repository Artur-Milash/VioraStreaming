import type {ModalProps} from "../../../types/modalTypes.ts";
import {Box, DialogActions, DialogContent, Typography} from "@mui/material";
import {Modal} from "../Modal/Modal.tsx";
import {VioraButton} from "../../Button/VioraButton.tsx";
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import {useTheme} from "@mui/material/styles";
import {AUTH_CONSTANTS} from "../../../constants/authConstants.ts";


export function EmailVerificationModal({data, onClose}: ModalProps) {
  const modalData = data as {
    description: string;
    onBtnClick: () => void;
    isLoading: boolean;
  };
  const theme = useTheme();

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
            <CheckCircleIcon
                sx={{color: theme.palette.success.main, fontSize: "25px"}}
            />
          </Box>

          <Typography variant="h5" color={"text.primary"} sx={{
            fontWeight: "bold"
          }}>
            {AUTH_CONSTANTS.CHECK_INBOX_TITLE}
          </Typography>

          <Typography variant="body1" color={"text.primary"} align="center">
            {modalData.description}
          </Typography>

        </DialogContent>

        <DialogActions>
          <VioraButton onClick={modalData.onBtnClick} name={AUTH_CONSTANTS.SEND_AGAIN_BUTTON} type={"button"}
                       loading={modalData.isLoading}/>
        </DialogActions>
      </Modal>
  )

}