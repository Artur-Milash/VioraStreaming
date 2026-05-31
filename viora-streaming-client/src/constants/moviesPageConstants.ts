export const MOVIES_PAGE_CONSTANTS = {
  // FilterPanel
  FILTER_TITLE: "Filters",
  FILTER_RATING_LABEL: "Rating",
  FILTER_RELEASE_YEAR_LABEL: "Release Year",
  FILTER_DURATION_LABEL: "Duration",
  FILTER_GENRES_LABEL: "Genres",
  FILTER_RESET_BUTTON: "Reset all filters",
  FILTER_SHOW_LESS: "Show less",
  FILTER_SHOW_MORE: "Other",

  // ActiveFilters
  ACTIVE_FILTERS_LABEL: "Active Filters:",
  CLEAR_ALL_BUTTON: "Clear all",

  // TrendingMovies (no results state)
  NO_MOVIES_ALT: "No movies match found",
  NO_MOVIES_TITLE: "No movies match",
  NO_MOVIES_DESCRIPTION:
    "We couldn't find any titles that match your current selection. Try broadening your criteria or resetting your search.",
  CLEAR_ALL_FILTERS_BUTTON: "Clear all filters",
  TRENDING_INSTEAD_TITLE: "Trending instead",

  // MovieContent & TrendingMovies (shared)
  LOAD_ERROR: "Failed to load movies. Please try again.",
  END_OF_LIST: "You've reached the end",
} as const;
