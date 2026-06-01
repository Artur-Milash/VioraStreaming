import {FormContainer} from "./FormContainer.tsx";
import {Alert, Box, Divider, Link, Stack, Typography} from "@mui/material";
import {Controller, useForm} from "react-hook-form";
import {CustomField} from "../../../components/Field/CustomField.tsx";
import {VioraButton} from "../../../components/Button/VioraButton.tsx";
import {RulesLinearProgress} from "../../../components/LinearProgress/RulesLinearProgress.tsx";
import {AUTH_CONSTANTS} from "../../../constants/authConstants.ts";

type FormData = {
  email: string;
  password: string;
  confirmPassword: string;
};

type RegisterFormProps = {
  onSubmit: (data: FormData) => void;
  isLoading?: boolean;
  error?: string;
  onSignIn?: () => void;
};

export function RegisterForm({onSubmit, isLoading, error, onSignIn}: RegisterFormProps) {
  const {handleSubmit, control, getValues, formState: {errors}} = useForm<FormData>();

  return (
      <FormContainer>
        <Stack spacing={"32px"}>
          <Stack spacing={"5px"}>
            <Typography variant="h5" align="center" sx={{fontWeight: 700}}>
              {AUTH_CONSTANTS.REGISTER_TITLE}
            </Typography>
            <Typography variant="body2" color={"text.secondary"} align="center">
              {AUTH_CONSTANTS.REGISTER_SUBTITLE}
            </Typography>
          </Stack>

          {
              (Object.keys(errors).length > 0 || error) && (
                  <Alert severity="error" variant="filled" sx={{
                    backgroundColor: "error.main",
                    fontWeight: "bold"
                  }}>
                    {error ? error : errors[Object.keys(errors)[0] as keyof typeof errors]?.message}
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
                          type='email'
                          error={fieldState.error}
                      />
                  )}
              />

              <Controller
                  name="password"
                  control={control}
                  defaultValue=""
                  rules={{
                    required: AUTH_CONSTANTS.PASSWORD_REQUIRED,
                    validate: (value: string) => value.length >= 8 || AUTH_CONSTANTS.PASSWORD_MIN_LENGTH_ERROR
                  }}
                  render={({field, fieldState}) => (
                      <Stack spacing={"10px"} sx={{
                        alignItems: "center"
                      }}>
                        <CustomField
                            {...field}
                            fullWidth
                            type="password"
                            label={AUTH_CONSTANTS.PASSWORD_LABEL}
                            error={fieldState.error}
                        />
                        <RulesLinearProgress
                            rules={4}
                            fullFilled={field.value.length / 2}
                            message={fieldState.error && AUTH_CONSTANTS.PASSWORD_MIN_LENGTH_HINT}/>
                      </Stack>
                  )}
              />

              <Controller
                  name="confirmPassword"
                  control={control}
                  defaultValue=""
                  rules={{
                    required: AUTH_CONSTANTS.CONFIRM_PASSWORD_REQUIRED,
                    validate: (value: string) => value ===  getValues("password") || AUTH_CONSTANTS.PASSWORDS_MISMATCH
                  }}
                  render={({field, fieldState}) => (
                      <CustomField
                          {...field}
                          fullWidth
                          type="password"
                          label={AUTH_CONSTANTS.CONFIRM_PASSWORD_LABEL}
                          error={fieldState.error}
                      />
                  )}
              />

              <VioraButton name={AUTH_CONSTANTS.CREATE_ACCOUNT_BUTTON} loading={isLoading}/>
            </Stack>
          </form>
        </Stack>

        <Stack spacing={"32px"} sx={{mt: 2}}>
          <Divider>
            <Box sx={{
              padding: "0 16px",
              backgroundColor: "#201F21"
            }}>
              <Typography variant="body2">{AUTH_CONSTANTS.OR_DIVIDER}</Typography>
            </Box>
          </Divider>

          <Stack direction="row" spacing={"8px"} sx={{
            justifyContent: "center",
            alignItems: "center",
          }}>
            <Typography variant="body2" color={"textDisabled"}>
              {AUTH_CONSTANTS.ALREADY_HAVE_ACCOUNT_TEXT}
            </Typography>
            <Link component="button"
                  color="primary"
                  underline={"none"}
                  onClick={onSignIn}>
              {AUTH_CONSTANTS.SIGN_IN_LINK}
            </Link>
          </Stack>
        </Stack>

      </FormContainer>
  )
}
