package org.viora.viorastreamingcore.ai.dto;

import jakarta.validation.constraints.NotNull;

public record CreateDiscussionRequest(@NotNull Long movieId) {}
