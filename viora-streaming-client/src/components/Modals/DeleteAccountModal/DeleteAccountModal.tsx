import {Modal} from "../Modal/Modal.tsx";
import {Button, Container, Stack, Typography} from "@mui/material";
import type {ModalProps} from "../../../types/modalTypes.ts";
import ReportProblemIcon from "@mui/icons-material/ReportProblem";
import {DELETE_ACCOUNT_DESCRIPTION, SETTINGS_UI} from "../../../constants/settingsConstants.ts";
import {SettingsField} from "../../Field/SettingsField.tsx";
import {useState} from "react";

const CONFIRM_KEYWORD = SETTINGS_UI.DELETE_CONFIRM_PLACEHOLDER;

export function DeleteAccountModal({data, onClose}: ModalProps) {
  const {onDelete} = data as { onDelete: () => void };

  const [inputValue, setInputValue] = useState("");
  const isConfirmed = inputValue === CONFIRM_KEYWORD;

  const handleDelete = () => {
    if (!isConfirmed) return;
    onClose();
    onDelete();
  };

  return (
      <Modal onClose={onClose}>
        <Container
            sx={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              height: "100%",
            }}
        >
          <Stack sx={{alignItems: "center", maxWidth: "340px"}} spacing="24px">
            <ReportProblemIcon color="error" sx={{fontSize: "55px"}}/>

            <Stack spacing="12px" sx={{textAlign: "center"}}>
              <Typography
                  variant="h5"
                  sx={{fontWeight: "bold", color: "#E5E1E4"}}
              >
                {SETTINGS_UI.DELETE_MODAL_TITLE}
              </Typography>

              <Typography
                  variant="body2"
                  sx={{textAlign: "center", color: "#E5E1E4"}}
              >
                {DELETE_ACCOUNT_DESCRIPTION}
              </Typography>
            </Stack>

            <SettingsField
                label={SETTINGS_UI.DELETE_CONFIRM_LABEL}
                placeholder={SETTINGS_UI.DELETE_CONFIRM_PLACEHOLDER}
                fullWidth
                value={inputValue}
                onChange={setInputValue}
            />

            <Stack
                direction="row"
                spacing="12px"
                sx={{justifyContent: "space-between", width: "100%"}}
            >
              <Button
                  variant="contained"
                  onClick={onClose}
                  sx={{
                    p: "12px 53px",
                    backgroundColor: "#353437",
                    borderRadius: "12px",
                    textTransform: "none",
                    fontWeight: "bold",
                  }}
              >
                {SETTINGS_UI.CANCEL_BUTTON}
              </Button>
              <Button
                  variant="contained"
                  color="error"
                  onClick={handleDelete}
                  disabled={!isConfirmed}
                  sx={{
                    p: "12px 21px",
                    borderRadius: "12px",
                    textTransform: "none",
                    fontWeight: "bold",
                  }}
              >
                {SETTINGS_UI.CONFIRM_DELETE_BUTTON}
              </Button>
            </Stack>
          </Stack>
        </Container>
      </Modal>
  );
}