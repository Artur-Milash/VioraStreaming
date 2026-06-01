import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { getHistoryByMovie, getUserHistories, saveMovieToHistory } from "../api/historyApi.ts";
import type { History } from "../types/historyTypes.ts";
import { useToast } from "./useToast.ts";

export const historyKeys = {
  all: ["history"] as const,
  byMovie: (movieId: number) => ["history", "movie", movieId] as const,
};

export const useHistory = () => {
  const { data: histories = [], isLoading } = useQuery({
    queryKey: historyKeys.all,
    queryFn: getUserHistories,
  });

  return { histories: histories as History[], isLoading };
};

export const useHistoryByMovie = (movieId: number) => {
  const { data: history = null, isLoading } = useQuery({
    queryKey: historyKeys.byMovie(movieId),
    queryFn: () => getHistoryByMovie(movieId),
  });

  return { history: history as History | null, isLoading };
};

export const useSaveToHistory = () => {
  const queryClient = useQueryClient();
  const toast = useToast();

  const { mutate: saveToHistory, isPending } = useMutation({
    mutationFn: (movieId: number) => saveMovieToHistory(movieId),
    onSuccess() {
      queryClient.invalidateQueries({ queryKey: historyKeys.all });
      toast("Added to My List");
    },
  });

  return { saveToHistory, isPending };
};