export const ERROR_PAGE_CONSTANTS = {
  BACK_TO_HOME_BUTTON: "Back to Home",
  FALLBACK_TITLE: "Something Went Wrong",
  FALLBACK_DESCRIPTION: "An unexpected error occurred.",
} as const;

export const ERROR_MESSAGES: Record<number, { title: string; description: string }> = {
  404: {
    title: "Page Not Found",
    description: "The page you're looking for doesn't exist or has been moved.",
  },
  401: {
    title: "Unauthorized",
    description: "You don't have permission to view this page.",
  },
  403: {
    title: "Forbidden",
    description: "Access to this resource is denied.",
  },
  500: {
    title: "Server Error",
    description: "Something went wrong on our end. Please try again later.",
  },
};
