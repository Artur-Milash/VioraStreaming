export const AUTH_CONSTANTS = {
  MAIL_SENT_MESSAGE:
      "We have sent you an email to confirm your account. Please check your inbox and follow the instructions to activate your account.",
  DROP_PASSWORD_MESSAGE: "We’ve sent a password reset link to ",
  PASSWORD_RESET_SUCCESS_MESSAGE: "Your password has been reset successfully.",

  // Login form
  LOGIN_TITLE: "Welcome back",
  LOGIN_ERROR_MESSAGE: "Invalid email or password. Please try again later",
  SIGN_IN_BUTTON: "Sign In",
  FORGOT_PASSWORD_LINK: "Forgot password?",
  NO_ACCOUNT_TEXT: "Don’t have an account?",
  CREATE_ACCOUNT_LINK: "Create account",

  // Register form
  REGISTER_TITLE: "Create your account",
  REGISTER_SUBTITLE: "Join the premium streaming experience",
  CREATE_ACCOUNT_BUTTON: "Create Account",
  OR_DIVIDER: "OR",
  ALREADY_HAVE_ACCOUNT_TEXT: "Already have an account?",
  SIGN_IN_LINK: "Sign in",

  // Forgot password form
  FORGOT_PASSWORD_BACK_LINK: "Back to Login",
  FORGOT_PASSWORD_TITLE: "Forgot your Password",
  FORGOT_PASSWORD_SUBTITLE: "No worries, we’ll send you reset instructions.",
  SEND_RESET_LINK_BUTTON: "Send Reset Link",

  // Set new password form
  NEW_PASSWORD_TITLE: "Set new password",
  NEW_PASSWORD_SUBTITLE: "Your new password must be different from previous passwords",
  NEW_PASSWORD_LABEL: "New password",
  RESET_PASSWORD_BUTTON: "Reset password",
  BACK_TO_LOGIN_LINK: "Back to login",

  // Shared field labels
  EMAIL_LABEL: "Email",
  EMAIL_PLACEHOLDER: "name@company.com",
  PASSWORD_LABEL: "Password",
  CONFIRM_PASSWORD_LABEL: "Confirm Password",

  // Shared validation messages
  EMAIL_REQUIRED: "Email is required",
  PASSWORD_REQUIRED: "Password is required",
  PASSWORD_MIN_LENGTH_ERROR: "Password must be at least 8 characters",
  PASSWORD_MIN_LENGTH_HINT: "Must be at least 8 characters",
  CONFIRM_PASSWORD_REQUIRED: "Please confirm your password",
  PASSWORDS_MISMATCH: "Passwords do not match",

  // Email verification modal
  CHECK_INBOX_TITLE: "Check your inbox",
  SEND_AGAIN_BUTTON: "Send Again",

  // Network error modal
  NETWORK_ERROR_TITLE: "Network Error",
  CLOSE_BUTTON: "Close",
} as const;