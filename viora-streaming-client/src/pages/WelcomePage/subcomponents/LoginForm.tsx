import {
  Box,
  Typography,
  Link, Stack, Alert,
} from "@mui/material";
import {useForm, Controller} from "react-hook-form";
import {FormContainer} from "./FormContainer.tsx";
import {CustomField} from "../../../components/Field/CustomField.tsx";
import {VioraButton} from "../../../components/Button/VioraButton.tsx";
import {AUTH_CONSTANTS} from "../../../constants/authConstants.ts";

type FormData = {
  email: string;
  password: string;
  remember: boolean;
};

type LoginFormProps = {
  onSubmit: (data: FormData) => void;
  isLoading?: boolean;
  error?: string;
  onCreateAccount?: () => void;
  onForgotPassword?: () => void;
};

export default function LoginForm({
                                    onSubmit,
                                    isLoading,
                                    error,
                                    onCreateAccount,
                                    onForgotPassword
                                  }: LoginFormProps) {
  const {handleSubmit, control, formState: {errors}} = useForm<FormData>();

  return (
      <FormContainer>
        <Stack spacing={"32px"}>
          <Typography variant="h5" align="center" sx={{fontWeight: 700}}>
            {AUTH_CONSTANTS.LOGIN_TITLE}
          </Typography>

          {
              (Object.keys(errors).length > 0 || error) && (
                  <Alert severity="error" variant="filled" sx={{
                    backgroundColor: "error.main",
                    fontWeight: "bold"
                  }}>
                    {AUTH_CONSTANTS.LOGIN_ERROR_MESSAGE}
                  </Alert>
              )
          }

          <form onSubmit={handleSubmit(onSubmit)}>
            <Stack spacing={"24px"}>
              <Controller
                  name="email"
                  control={control}
                  defaultValue=""
                  rules={{
                    required: AUTH_CONSTANTS.EMAIL_REQUIRED
                  }}
                  render={({field, fieldState}) => (
                      <CustomField
                          {...field}
                          label={AUTH_CONSTANTS.EMAIL_LABEL}
                          placeholder={AUTH_CONSTANTS.EMAIL_PLACEHOLDER}
                          type="email"
                          error={fieldState.error}
                      />
                  )}
              />

              <Controller
                  name="password"
                  control={control}
                  defaultValue=""
                  rules={{
                    required: AUTH_CONSTANTS.PASSWORD_REQUIRED
                  }}
                  render={({field, fieldState}) => (
                      <CustomField
                          {...field}
                          fullWidth
                          type="password"
                          label={AUTH_CONSTANTS.PASSWORD_LABEL}
                          error={fieldState.error}
                      />
                  )}
              />

              <Box
                  sx={{
                    display: "flex",
                    justifyContent: "end",
                  }}
              >
                <Link onClick={onForgotPassword} color="primary" underline="none" sx={{
                  fontWeight: "bold"
                }}>
                  {AUTH_CONSTANTS.FORGOT_PASSWORD_LINK}
                </Link>
              </Box>

              <VioraButton name={AUTH_CONSTANTS.SIGN_IN_BUTTON} loading={isLoading}/>
            </Stack>
          </form>

          <Stack sx={{
            justifyContent: "center",
            alignItems: "center",
          }}>
            <Typography variant="body2" color={"text.secondary"}>
              {AUTH_CONSTANTS.NO_ACCOUNT_TEXT}
            </Typography>
            <Link component="button"
                  color="primary"
                  underline={"none"}
                  onClick={onCreateAccount}>
              {AUTH_CONSTANTS.CREATE_ACCOUNT_LINK}
            </Link>
          </Stack>
        </Stack>
      </FormContainer>
  );
}