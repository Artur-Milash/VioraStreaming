package org.viora.viorastreamingcore.content.dto;

import java.time.LocalDate;
import java.util.Set;

public record MovieDto(
    Long id,
    String title,
    String plot,
    String poster,
    String rated,
    Float rating,
    String videoUrl,
    LocalDate releaseDate,
    Long durationInMinutes,
    Set<ActorDto> actors,
    DirectorDto director,
    Set<GenreDto> genres,
    WriterDto writer,
    String imdbId
) {

}
