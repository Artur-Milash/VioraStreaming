import {Controller, useForm} from "react-hook-form";
import {FormContainer} from "./FormContainer.tsx";
import {Alert, Link, Stack, Typography} from "@mui/material";
import {CustomField} from "../../../components/Field/CustomField.tsx";
import {RulesLinearProgress} from "../../../components/LinearProgress/RulesLinearProgress.tsx";
import {VioraButton} from "../../../components/Button/VioraButton.tsx";
import KeyboardBackspaceIcon from '@mui/icons-material/KeyboardBackspace';
import {AUTH_CONSTANTS} from "../../../constants/authConstants.ts";

type FormData = {
  password: string;
  confirmPassword: string;
};

type DropPasswordFormProps = {
  onSubmit: (data: FormData) => void;
  isLoading?: boolean;
  error?: string;
  onSignIn?: () => void;
};

export function DropPasswordForm({onSubmit, isLoading, error, onSignIn}: DropPasswordFormProps) {
  const {handleSubmit, control, getValues, formState: {errors}} = useForm<FormData>();

  return (
      <FormContainer>
        <Stack spacing={"32px"}>
          <Stack spacing={"5px"}>
            <Typography variant="h5" align="center" sx={{fontWeight: 700}}>
              {AUTH_CONSTANTS.NEW_PASSWORD_TITLE}
            </Typography>
            <Typography variant="body2" align="center" sx={{
              color: "#CCC3D8",
              maxWidth: "90%",
            }}>
              {AUTH_CONSTANTS.NEW_PASSWORD_SUBTITLE}
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
                            label={AUTH_CONSTANTS.NEW_PASSWORD_LABEL}
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
                    validate: (value: string) => value === getValues("password") || AUTH_CONSTANTS.PASSWORDS_MISMATCH
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

              <VioraButton name={AUTH_CONSTANTS.RESET_PASSWORD_BUTTON} loading={isLoading}/>
            </Stack>
          </form>
        </Stack>

        <Stack spacing={"32px"} sx={{mt: 3}}>

          <Stack direction="row" spacing={"8px"} sx={{
            justifyContent: "center",
            alignItems: "center",
          }}>
            <KeyboardBackspaceIcon color="primary"/>
            <Link component="button"
                  color="primary"
                  underline={"none"}
                  onClick={onSignIn}>
              {AUTH_CONSTANTS.BACK_TO_LOGIN_LINK}
            </Link>
          </Stack>
        </Stack>

      </FormContainer>
  )
}
