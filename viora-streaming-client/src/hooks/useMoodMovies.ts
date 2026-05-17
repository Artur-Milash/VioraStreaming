import {useMutation} from "@tanstack/react-query";
import {fetchMoodMovies} from "../api/aiApi.ts";
import type {MoodMovieSuggestion} from "../types/movieTypes.ts";

type UseMoodMoviesResult = {
  suggestions: MoodMovieSuggestion[];
  isLoading: boolean;
  findMovies: (mood: string) => void;
};

export function useMoodMovies(): UseMoodMoviesResult {
  const {mutate, data, isPending} = useMutation({
    mutationFn: (mood: string) => fetchMoodMovies(mood),
  });

  return {
    suggestions: data ?? [],
    isLoading: isPending,
    findMovies: mutate,
  };
}
