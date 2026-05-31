import {Box, Button, CircularProgress, Stack, Typography} from "@mui/material";
import {Movies} from "./Movies.tsx";
import NoMoviesImg from "../../../assets/no-movies.png";
import {useTrendingMovies} from "../../../hooks/useMovies.ts";
import {useDispatch} from "react-redux";
import type {AppDispatch} from "../../../store/store.ts";
import {resetFilters} from "../../../store/filterSlice.ts";
import {MOVIES_PAGE_CONSTANTS} from "../../../constants/moviesPageConstants.ts";

export function TrendingMovies() {

  const {movies, isLoading, isError, hasMore, loaderRef} = useTrendingMovies();
  const dispatch = useDispatch<AppDispatch>();

  return (
      <>
        <Stack
            spacing="24px"
            sx={{
              minHeight: "400px",
              textAlign: "center",
              mb: '60px',
              alignItems: "center",
              justifyContent: "center"
            }}
        >
          <Box
              component="img"
              src={NoMoviesImg}
              alt={MOVIES_PAGE_CONSTANTS.NO_MOVIES_ALT}
              sx={{
                width: "96px",
                height: "auto",
                display: "block",
                mb: "20px"
              }}
          />
          <Typography variant="h4" sx={{fontWeight: 800, fontSize: "36px"}}>
            {MOVIES_PAGE_CONSTANTS.NO_MOVIES_TITLE}
          </Typography>
          <Typography variant="body1" color="text.secondary"
                      sx={{maxWidth: "500px", fontSize: "16px", paddingBottom: "40px"}}>
            {MOVIES_PAGE_CONSTANTS.NO_MOVIES_DESCRIPTION}
          </Typography>
          <Button
              variant="contained"
              color="primary"
              sx={{
                textTransform: "none",
                borderRadius: "12px",
                p: "12px 24px",
                fontWeight: "bold",
              }}
              onClick={() => dispatch(resetFilters())}
          >
            {MOVIES_PAGE_CONSTANTS.CLEAR_ALL_FILTERS_BUTTON}
          </Button>
        </Stack>

        <Typography variant="h6" sx={{fontWeight: 700, mb: "24px"}}>
          {MOVIES_PAGE_CONSTANTS.TRENDING_INSTEAD_TITLE}
        </Typography>
        <Movies movies={movies}/>

        {/* ← Sentinel: hook watches this div */}
        <Box ref={loaderRef} sx={{py: "32px", display: "flex", justifyContent: "center"}}>
          {isLoading && <CircularProgress size={28}/>}
          {isError && (
              <Typography color="error">{MOVIES_PAGE_CONSTANTS.LOAD_ERROR}</Typography>
          )}
          {!hasMore && !isLoading && (
              <Typography color="text.secondary">{MOVIES_PAGE_CONSTANTS.END_OF_LIST}</Typography>
          )}
        </Box>
      </>
  )
}