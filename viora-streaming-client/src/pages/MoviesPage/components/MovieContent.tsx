import {Box, Typography, CircularProgress, Skeleton} from "@mui/material";
import {useInfiniteMovies} from "../../../hooks/useMovies.ts";
import {ActiveFilters} from "./ActiveFilters.tsx";
import {Movies} from "./Movies.tsx";
import {TrendingMovies} from "./TrendingMovies.tsx";
import {MOVIES_PAGE_CONSTANTS} from "../../../constants/moviesPageConstants.ts";

export function MovieContent() {
  const {movies, isLoading, isError, hasMore, loaderRef} = useInfiniteMovies();

  return (
      <Box sx={{flex: 1, p: "40px", backgroundColor: "background.default"}}>
        <ActiveFilters/>

        <Movies movies={movies}/>

        {isLoading && movies.length === 0 && (
            <MovieSkeletons count={10} />
        )}

        {movies.length === 0 && !isLoading && (
            <TrendingMovies />
        )}

        <Box ref={loaderRef} sx={{py: "32px", display: "flex", justifyContent: "center"}}>
          {isLoading && movies.length > 0 && <CircularProgress size={28}/>}
          {isError && (
              <Typography color="error">{MOVIES_PAGE_CONSTANTS.LOAD_ERROR}</Typography>
          )}
          {!hasMore && !isLoading && (
              <Typography color="text.secondary">{MOVIES_PAGE_CONSTANTS.END_OF_LIST}</Typography>
          )}
        </Box>

      </Box>
  );
}

function MovieSkeletons({ count }: { count: number }) {
  return (
      <Box sx={{ display: "flex", flexWrap: "wrap", gap: 2 }}>
        {Array.from({ length: count }).map((_, i) => (
            <Box key={i} sx={{ width: 200 }}>
              <Skeleton variant="rounded" width={200} height={300} />
              <Skeleton variant="text" sx={{ mt: 1 }} width="80%" />
              <Skeleton variant="text" width="50%" />
            </Box>
        ))}
      </Box>
  );
}