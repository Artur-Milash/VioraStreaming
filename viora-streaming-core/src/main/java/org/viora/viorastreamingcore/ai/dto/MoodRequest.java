package org.viora.viorastreamingcore.ai.dto;

import jakarta.validation.constraints.NotBlank;

public record MoodRequest(@NotBlank String mood) {}
