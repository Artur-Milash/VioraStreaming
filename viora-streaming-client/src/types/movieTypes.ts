export interface Genre {
  id: number;
  name: string;
}

export interface MovieSummary {
  id: number;
  title: string;
  poster: string;
  releaseDate: string;
  videoUrl?: string;
  genres: Genre[];
  rating: number;
  durationInMinutes: number;
  plot: string;
}

export interface Duration {
  from?: number;
  to?: number;
}

export interface Pageable {
  page: number;
  size: number;
}

export interface MovieFilter {
  search?: string;
  genresIds: number[];
  rating: number;
  releaseYear: ReleaseYear;
  duration: Duration;
  page: number;
  size: number;
}

export interface ReleaseYear {
  from: number;
  to: number;
}

export interface Person {
  id: number;
  name: string;
  photo: string;
}

export interface MoodMovieSuggestion {
  movie: MovieSummary;
  matchLabel: string;
}

export interface Discussion {
  id: number;
  movieId: number;
  createdAt: number;
}

export interface DiscussionMessage {
  id: number;
  role: "USER" | "AI";
  content: string;
  createdAt: number;
}

export interface MovieDetails {
  id: number,
  title: string,
  plot: string,
  poster: string,
  rating: number,
  rated: string,
  videoUrl: string,
  releaseDate: string,
  durationInMinutes: number,
  actors: Person[],
  director: Person,
  genres: Genre[],
  writer: Person,
  imdbId: string,
}