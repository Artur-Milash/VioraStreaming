import {apiFetch, apiPostWithoutResult} from "../utils/apiUtils.ts";
import {API_PATHS} from "../constants/apiConstants.ts";
import type {History} from "../types/historyTypes.ts";

export async function getUserHistories(): Promise<History[]> {
  return apiFetch(API_PATHS.history)
}

export async function getHistoryByMovie(movieId: number): Promise<History> {
  return apiFetch(`${API_PATHS.history}/${movieId}`)
}

export async function saveMovieToHistory(movieId: number): Promise<void> {
  return apiPostWithoutResult(`${API_PATHS.history}/${movieId}`, {method: "POST"})
}