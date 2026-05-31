export const MOVIE_DETAILS_PAGE_CONSTANTS = {
  PLAY_BUTTON: "Play",
  MY_LIST_BUTTON: "My List",
  FEATURED_CAST_TITLE: "Featured Cast",
  DURATION_SUFFIX: "Min.",

  // MovieOperators labels
  DIRECTOR_LABEL: "Director",
  WRITER_LABEL: "Writer",
  RATED_LABEL: "Rated",
  DURATION_LABEL: "Duration",

  // MovieAssistantEnabler
  ASSISTANT_TITLE: "Movie Assistant",
  DISCUSS_BUTTON: "Discuss with AI",
} as const;

export const getMovieAssistantContent = (title: string) =>
  `Have questions about "${title}"? Ask our AI about the plot, themes, characters, and more.`;
