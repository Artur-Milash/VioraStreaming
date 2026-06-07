package org.viora.viorastreamingcore.content.dto;

import lombok.Builder;
import java.time.LocalDate;
import java.util.Set;

@Builder
public record MovieSummary(
    Long id,
    String title,
    String poster,
    LocalDate releaseDate,
    String videoUrl,
    Set<GenreDto> genres,
    Float rating,
    Long durationInMinutes,
    String plot
) {

}
