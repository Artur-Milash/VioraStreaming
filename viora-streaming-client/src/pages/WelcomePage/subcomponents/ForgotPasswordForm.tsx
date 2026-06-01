import {FormContainer} from "./FormContainer.tsx";
import {Stack, Link, Container, Box, Typography, Button} from "@mui/material";
import KeyboardBackspaceIcon from '@mui/icons-material/KeyboardBackspace';
import MailIcon from '@mui/icons-material/Mail';
import {CustomField} from "../../../components/Field/CustomField.tsx";
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import {AUTH_CONSTANTS} from "../../../constants/authConstants.ts";

type ForgotPasswordFormProps = {
  onSubmit: (email: string) => void;
}

export function ForgotPasswordForm({onSubmit}: ForgotPasswordFormProps) {
  const [email, setEmail] = useState("");
  const navigate = useNavigate();

  const handleSubmit = () => {
    if (email.trim()) {
      onSubmit(email);
    }
  };

  return (
      <FormContainer>
        <Stack direction="row" sx={{
          alignItems: "start",
          color: "primary.main",
          pb: "32px",
          cursor: "pointer"
        }} spacing="10px" onClick={() => navigate(-1)}>
          <KeyboardBackspaceIcon/>
          <Link underline="none" sx={{fontWeight: "bold"}}>{AUTH_CONSTANTS.FORGOT_PASSWORD_BACK_LINK}</Link>
        </Stack>
        <Container>
          <Stack direction="row" sx={{justifyContent: "center", width: "100%", pb: "24px"}}>
            <MailFormIcon/>
          </Stack>
          <Stack spacing="12px" sx={{pb: "40px"}}>
            <Typography variant="h5" align="center" sx={{fontWeight: 700}}>
              {AUTH_CONSTANTS.FORGOT_PASSWORD_TITLE}
            </Typography>
            <Typography variant="body1" align="center" sx={{color: "#CCC3D8"}}>
              {AUTH_CONSTANTS.FORGOT_PASSWORD_SUBTITLE}
            </Typography>
          </Stack>

          <Stack spacing="24px">
            <CustomField
                label={AUTH_CONSTANTS.EMAIL_LABEL}
                placeholder={AUTH_CONSTANTS.EMAIL_PLACEHOLDER}
                type="email"
                fullWidth
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
            <Button
                variant="contained"
                sx={{borderRadius: "12px", py: "16px", textTransform: "none", fontWeight: "bold"}}
                onClick={handleSubmit}
                disabled={!email.trim()}
            >
              {AUTH_CONSTANTS.SEND_RESET_LINK_BUTTON}
            </Button>
          </Stack>
        </Container>
      </FormContainer>
  );
}

function MailFormIcon() {
  return (
      <Box sx={{
        display: "flex", justifyContent: "center", alignItems: "center",
        bgcolor: "#2A2A2C", height: "64px", width: "64px", borderRadius: "12px",
      }}>
        <MailIcon fontSize="large" color="primary"/>
      </Box>
  );
}