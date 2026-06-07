import {useMovie} from "./useMovie.ts";
import {useHistoryByMovie} from "./useHistory.ts";
import type {History} from "../types/historyTypes.ts";
import {useEffect, useState} from "react";

interface UsePlayerResult {
  imdbId: string | undefined;
  title: string | undefined;
  videoUrl?: string | null;
  isLoading: boolean;
  history?: History | null;
}

export const usePlayer = (id: number): UsePlayerResult => {
  const {movie, isLoading: movieLoading} = useMovie(id);
  const {history, isLoading: historyLoading} = useHistoryByMovie(id);

  const [isLoading, setIsLoading] = useState<boolean>(movieLoading || historyLoading);

  useEffect(() => {
    setIsLoading(movieLoading || historyLoading);
  }, [movieLoading, historyLoading]);

  return {
    imdbId: movie?.imdbId,
    title: movie?.title,
    videoUrl: movie?.videoUrl ?? null,
    isLoading,
    history
  };
};