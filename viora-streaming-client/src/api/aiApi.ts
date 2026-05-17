import {apiFetch} from "../utils/apiUtils.ts";
import {API_PATHS} from "../constants/apiConstants.ts";
import type {MoodMovieSuggestion} from "../types/movieTypes.ts";

export function fetchMoodMovies(mood: string): Promise<MoodMovieSuggestion[]> {
  return apiFetch(API_PATHS.moodMovies, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify({mood: mood}),
  });
}
