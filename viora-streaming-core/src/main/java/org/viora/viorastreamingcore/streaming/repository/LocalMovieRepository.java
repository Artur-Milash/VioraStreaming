package org.viora.viorastreamingcore.streaming.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

@Repository
public class LocalMovieRepository implements StreamingRepository {

  private static final int MINIMAL_SEGMENT_ID_LENGTH = 3;

  @Value("${streaming.segments-path}")
  private String rootSegmentsFolderPath;

  @Override
  @SneakyThrows
  public Resource getMoviePlayback(String movieId) {
    Path playlistPath = Paths.get(rootSegmentsFolderPath, movieId, "playlist.m3u8");
    if (!Files.exists(playlistPath)) {
      throw new IllegalArgumentException("Playlist not found for movie " + movieId);
    }
    return new FileSystemResource(playlistPath);
  }

  @Override
  @SneakyThrows
  public Resource getMovieSegment(String movieId, Long segment) {
    String segmentStringId = String.format("segment_%s.ts", getSegmentStringId(segment));
    Path segmentPath = Paths.get(rootSegmentsFolderPath, movieId, segmentStringId);
    if (!Files.exists(segmentPath)) {
      throw new IllegalArgumentException("Segment " + segment + " not found for movie " + movieId);
    }
    return new FileSystemResource(segmentPath);
  }

  private String getSegmentStringId(Long segment) {
    if (segment >= 100) {
      return segment.toString();
    }

    StringBuilder resultBuilder = new StringBuilder();
    long currentNumber = segment;
    while (currentNumber > 0) {
      long floatingNumber = currentNumber % 10;
      currentNumber /= 10;
      resultBuilder.append(floatingNumber);
    }

    int resultLength = resultBuilder.length();
    if (resultLength < MINIMAL_SEGMENT_ID_LENGTH) {
      resultBuilder.append("0".repeat(MINIMAL_SEGMENT_ID_LENGTH - resultLength));
    }

    resultBuilder.reverse();
    return resultBuilder.toString();
  }

}
