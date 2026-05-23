import {Stack, Typography, Link, Box, Grid, CircularProgress} from "@mui/material";
import {useTrendingMovies} from "../../hooks/useMovies.ts";
import {useHistory} from "../../hooks/useHistory.ts";
import {IntroMovie} from "./components/IntroMovie.tsx";
import type {MovieSummary} from "../../types/movieTypes.ts";
import {TrendingMovieCard} from "./components/TrendingMovieCard.tsx";
import {HistoryMovieCard} from "./components/HistoryMovieCard.tsx";
import type {History} from "../../types/historyTypes.ts";
import {NavLink, useNavigate} from "react-router-dom";
import {API_PAGE} from "../../constants/routingConstants.ts";
import {WeekendSpecial} from "./components/WeekendSpecial.tsx";
import {SmartRecommender} from "./components/SmartRecommender.tsx";
import {JoinTheCommunity} from "./components/JoinTheCommunity.tsx";

export default function HomePage() {
  const {movies, isLoading} = useTrendingMovies();
  const {histories, isLoading: historyLoading} = useHistory();

  if (isLoading || historyLoading) {
    return (
        <Box sx={{display: "flex", justifyContent: "center", alignItems: "center", height: "100vh"}}>
          <CircularProgress />
        </Box>
    );
  }

  return (
      <Stack spacing="48px">
        <IntroMovie movie={movies[0]}/>
        <Stack spacing="64px" sx={{p: "64px"}}>
          <ContinueWatchingSection histories={histories}/>
          <TrendingNowSection movies={movies}/>
          <CuratedForYou movie={movies[1]}/>
        </Stack>
      </Stack>
  );
}


type ContinueWatchingProps = { histories: History[] };

function ContinueWatchingSection({histories}: ContinueWatchingProps) {
  return (
      <Stack spacing="24px">
        <Stack direction="row" sx={{justifyContent: "space-between", alignItems: "center"}}>
          <Typography variant="h5" sx={{fontWeight: "bold"}}>Continue Watching</Typography>
          <Link component={NavLink} to={API_PAGE.History} underline="none">
            View All
          </Link>
        </Stack>

        {/*
        MovieCard is aspectRatio 2/3 with width filling the cell.
        At minmax(160px, 1fr) the card height ≈ 160 * 1.5 = 240px.
        Text below adds ~56px. Total row ≈ 296px + 24px gap = 320px.
        Setting height to that value clips any second row cleanly.
      */}
        <Box
            sx={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fill, minmax(160px, 1fr))",
              gap: "24px",
              height: "320px",   // card (240) + text (56) + gap buffer
              overflow: "hidden",
              alignItems: "start",
            }}
        >
          {histories.map((history) => (
              <HistoryMovieCard
                  history={history}
                  onClick={() => {
                  }}
              />
          ))}
        </Box>
      </Stack>
  );
}

type TrendingNowProps = { movies: MovieSummary[] };

function TrendingNowSection({movies}: TrendingNowProps) {
  const navigate = useNavigate();

  return (
      <Stack spacing="24px">
        <Typography variant="h5" sx={{fontWeight: "bold"}}>Trending Now</Typography>

        <Box
            sx={{
              display: "grid",
              gridTemplateColumns: "repeat(auto-fill, minmax(160px, 1fr))",
              gap: "24px",
              height: "320px",
              overflow: "hidden",
              alignItems: "start",
            }}
        >
          {movies.map((movie) => (
              <TrendingMovieCard
                  key={movie.id}
                  movie={movie}
                  onClick={() => navigate(`${API_PAGE.Movies}/${movie.id}`)}
              />
          ))}
        </Box>
      </Stack>
  );
}

type CuratedForYouProps = { movie: MovieSummary };

function CuratedForYou({movie}: CuratedForYouProps) {
  return (
      <Stack spacing="24px">
        <Typography variant="h6" sx={{fontWeight: "bold"}}>Curated For You</Typography>

        <Grid container spacing={3}>
          <Grid size={6}>
            <WeekendSpecial movie={movie}/>
          </Grid>
          <Grid size={3}>
            <SmartRecommender/>
          </Grid>
          <Grid size={3}>
            <JoinTheCommunity/>
          </Grid>
        </Grid>
      </Stack>
  );
}