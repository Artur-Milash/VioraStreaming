import {apiFetch} from "../utils/apiUtils.ts";
import {API_PATHS} from "../constants/apiConstants.ts";
import type {Discussion, DiscussionMessage} from "../types/movieTypes.ts";

export function createOrGetDiscussion(movieId: number): Promise<Discussion> {
  return apiFetch(API_PATHS.discussions, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify({movieId}),
  });
}

export function fetchMessages(discussionId: number): Promise<DiscussionMessage[]> {
  return apiFetch(`${API_PATHS.discussions}/${discussionId}/messages`);
}

export function sendMessage(discussionId: number, content: string): Promise<DiscussionMessage> {
  return apiFetch(`${API_PATHS.discussions}/${discussionId}/messages`, {
    method: "POST",
    headers: {"Content-Type": "application/json"},
    body: JSON.stringify({content}),
  });
}
