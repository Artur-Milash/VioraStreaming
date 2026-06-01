import {useState} from "react";
import {useParams, useNavigate} from "react-router";
import {useMovie} from "../../hooks/useMovie.ts";
import {useSaveToHistory} from "../../hooks/useHistory.ts";
import {Box, Button, Chip, CircularProgress, Stack, Typography} from "@mui/material";
import type {MovieDetails, Person} from "../../types/movieTypes.ts";
import AddIcon from "@mui/icons-material/Add";
import PlayArrowIcon from "@mui/icons-material/PlayArrow";
import {MovieAssistantEnabler} from "./components/MovieAssistantEnabler.tsx";
import {MovieDiscussionPanel} from "./components/MovieDiscussionPanel.tsx";
import {API_PAGE} from "../../constants/routingConstants.ts";
import {MOVIE_DETAILS_PAGE_CONSTANTS, getMovieAssistantContent} from "../../constants/movieDetailsPageConstants.ts";

export function MovieDetailsPage() {
  const {id} = useParams<{ id: string }>();
  const navigate = useNavigate();
  const {movie, isLoading} = useMovie(Number(id));
  const {saveToHistory, isPending: isSaving} = useSaveToHistory();
  const [discussionOpen, setDiscussionOpen] = useState(false);

  if (isLoading || !movie) {
    return (
        <Box sx={{display: "flex", justifyContent: "center", alignItems: "center", height: "100vh"}}>
          <CircularProgress />
        </Box>
    );
  }

  const handlePlay = () => {
    navigate(`/${API_PAGE.Movies}/${id}/player`);
  };

  return (
      <Box sx={{display: "flex", minHeight: "100%"}}>
        <Box sx={{flex: 1, py: "50px", px: "48px", minWidth: 0}}>
          <Stack spacing="64px" sx={{maxWidth: "1280px", margin: "0 auto"}}>
            <MovieDetails
              movie={movie}
              onPlay={handlePlay}
              onDiscuss={() => setDiscussionOpen(true)}
              onAddToList={() => saveToHistory(movie.id)}
              isSaving={isSaving}
          />
            <MovieCast actors={movie?.actors ?? []}/>
          </Stack>
        </Box>
        {discussionOpen && (
            <MovieDiscussionPanel
                movieId={movie.id}
                onClose={() => setDiscussionOpen(false)}
                sx={{position: "sticky", top: "80px", height: "calc(100vh - 80px)"}}
            />
        )}
      </Box>
  );
}

type MovieDetailsProps = {
  movie: MovieDetails;
  onPlay: () => void;
  onDiscuss: () => void;
  onAddToList: () => void;
  isSaving: boolean;
};

function MovieDetails({movie, onPlay, onDiscuss, onAddToList, isSaving}: MovieDetailsProps) {
  return (
      <Stack
          spacing="48px"
          direction="row"
          sx={{alignItems: "center", justifyContent: "center"}}
      >
        <Box
            sx={{
              height: "360px",
              width: "240px",
              backgroundImage: `url(${movie.poster})`,
              backgroundSize: "cover",
              backgroundPosition: "center",
              overflow: "hidden",
              boxShadow: "0px 8px 24px rgba(0, 0, 0, 0.4)",
              borderRadius: "8px",
            }}
        />

        <Stack spacing="32px">
          <Stack spacing="16px" sx={{maxWidth: "650px"}}>
            <MovieLabels movie={movie}/>
            <Typography variant="h2" sx={{fontWeight: "bolder"}}>
              {movie.title}
            </Typography>
            <Typography variant="body1">{movie.plot}</Typography>
          </Stack>

          <MovieOperators movie={movie}/>

          <Stack spacing="16px" direction="row">
            {/* Play — navigates to the player route */}
            <Button
                variant="contained"
                onClick={onPlay}
                sx={{
                  textTransform: "none",
                  fontWeight: "bold",
                  p: "12px 32px",
                  borderRadius: "12px",
                }}
            >
              <PlayArrowIcon sx={{mr: 0.5}}/>
              {MOVIE_DETAILS_PAGE_CONSTANTS.PLAY_BUTTON}
            </Button>

            <Button
                variant="outlined"
                onClick={onAddToList}
                disabled={isSaving}
                sx={{
                  textTransform: "none",
                  fontWeight: "bold",
                  p: "12px 32px",
                  color: "text.primary",
                  borderRadius: "12px",
                }}
            >
              <AddIcon/>
              {MOVIE_DETAILS_PAGE_CONSTANTS.MY_LIST_BUTTON}
            </Button>
          </Stack>
        </Stack>

        <MovieAssistantEnabler
            content={getMovieAssistantContent(movie.title)}
            onButtonClick={onDiscuss}
        />
      </Stack>
  );
}

// ─── Sub-components (unchanged from your original) ───────────────────────────

type MovieCastProps = { actors: Person[] };

function MovieCast({actors}: MovieCastProps) {
  return (
      <Stack spacing="32px">
        <Typography variant="h5" sx={{fontWeight: "bold"}}>
          {MOVIE_DETAILS_PAGE_CONSTANTS.FEATURED_CAST_TITLE}
        </Typography>
        <Stack spacing="24px" direction="row">
          {actors.map((actor) => (
              <ActorCard actor={actor}/>
          ))}
        </Stack>
      </Stack>
  );
}

function MovieLabels({movie}: { movie: MovieDetails }) {
  return (
      <Stack spacing="12px" direction="row">
        {[
          movie.genres[0].name,
          movie.releaseDate.split("-")[0],
          movie.rating.toFixed(1),
        ].map((label) => (
            <Chip
                key={label}
                size="small"
                label={label}
                sx={{
                  backgroundColor: "#6B7280",
                  color: "text.primary",
                  fontWeight: "bold",
                  borderRadius: "6px",
                  "& .MuiChip-label": {px: "8px", py: "4px"},
                }}
            />
        ))}
      </Stack>
  );
}

function MovieOperators({movie}: { movie: MovieDetails }) {
  const operators = [
    {label: MOVIE_DETAILS_PAGE_CONSTANTS.DIRECTOR_LABEL, value: movie.director.name},
    {label: MOVIE_DETAILS_PAGE_CONSTANTS.WRITER_LABEL, value: movie.writer.name},
    {label: MOVIE_DETAILS_PAGE_CONSTANTS.RATED_LABEL, value: movie.rated},
    {label: MOVIE_DETAILS_PAGE_CONSTANTS.DURATION_LABEL, value: `${movie.durationInMinutes} ${MOVIE_DETAILS_PAGE_CONSTANTS.DURATION_SUFFIX}`},
  ];

  return (
      <Stack sx={{justifyContent: "space-between"}} direction="row">
        {operators.map((op) => (
            <Stack key={op.label} spacing="8px">
              <Typography variant="subtitle1" sx={{color: "text.secondary"}}>
                {op.label}
              </Typography>
              <Typography variant="body1" sx={{fontWeight: "bold"}}>
                {op.value}
              </Typography>
            </Stack>
        ))}
      </Stack>
  );
}

type ActorCardProps = { actor: Person };

function ActorCard({actor}: ActorCardProps) {
  return (
      <Stack spacing="16px">
        <Box
            sx={{
              height: "205px",
              width: "180px",
              backgroundImage: `url(${actor.photo})`,
              backgroundSize: "cover",
              backgroundPosition: "center",
              overflow: "hidden",
            }}
        />
        <Box>
          <Typography variant="body1" sx={{fontWeight: "bold"}}>
            {actor.name}
          </Typography>
        </Box>
      </Stack>
  );
}