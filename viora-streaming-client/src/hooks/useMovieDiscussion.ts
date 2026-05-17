import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {createOrGetDiscussion, fetchMessages, sendMessage} from "../api/discussionApi.ts";
import type {DiscussionMessage} from "../types/movieTypes.ts";

export function useMovieDiscussion(movieId: number | null) {
  const queryClient = useQueryClient();

  const discussionQuery = useQuery({
    queryKey: ["discussion", movieId],
    queryFn: () => createOrGetDiscussion(movieId!),
    enabled: movieId !== null,
  });

  const discussionId = discussionQuery.data?.id ?? null;

  const messagesQuery = useQuery({
    queryKey: ["discussion-messages", discussionId],
    queryFn: () => fetchMessages(discussionId!),
    enabled: discussionId !== null,
  });

  const sendMutation = useMutation({
    mutationFn: (content: string) => sendMessage(discussionId!, content),
    onSuccess: (aiMessage: DiscussionMessage) => {
      queryClient.setQueryData(
        ["discussion-messages", discussionId],
        (prev: DiscussionMessage[] = []) => [...prev, aiMessage],
      );
    },
  });

  const send = (content: string) => {
    if (!discussionId) return;
    queryClient.setQueryData(
      ["discussion-messages", discussionId],
      (prev: DiscussionMessage[] = []) => [
        ...prev,
        {id: Date.now(), role: "USER", content, createdAt: Date.now()} as DiscussionMessage,
      ],
    );
    sendMutation.mutate(content);
  };

  return {
    messages: messagesQuery.data ?? [],
    isLoadingDiscussion: discussionQuery.isPending,
    isLoadingMessages: messagesQuery.isPending,
    isSending: sendMutation.isPending,
    send,
  };
}
