package org.viora.viorastreamingcore.ai.dto;

import org.viora.viorastreamingcore.content.dto.MovieSummary;

public record MoodMovieSuggestion(MovieSummary movie, String matchLabel) {}
