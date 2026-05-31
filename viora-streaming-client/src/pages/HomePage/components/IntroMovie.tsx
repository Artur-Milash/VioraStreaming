import type {MovieSummary} from "../../../types/movieTypes.ts";
import {Box, Button, Chip, Stack, Typography} from "@mui/material";
import {API_PAGE} from "../../../constants/routingConstants.ts";
import {useNavigate} from "react-router-dom";
import PlayArrowIcon from "@mui/icons-material/PlayArrow";
import AddIcon from "@mui/icons-material/Add";
import {HOME_PAGE_CONSTANTS} from "../../../constants/homePageConstants.ts";

type IntroMovieProps = {
  movie: MovieSummary;
}

export function IntroMovie({movie}: IntroMovieProps) {
  const navigate = useNavigate();
  const handlePlay = () => {
    navigate(`/${API_PAGE.Movies}/${movie.id}/player`);
  };
  return (
      <Box sx={{
        width: "100%",
        bgcolor: "background.paper",
        p: "32px 48px"
      }}>
        <Stack spacing="24px" sx={{maxWidth: "900px"}}>
          <Stack>
            <Typography variant="subtitle2" sx={{
              color: "primary.main",
              fontWeight: "bold"
            }}>
              {HOME_PAGE_CONSTANTS.VIORA_ORIGINAL_LABEL}
            </Typography>
            <Typography variant="h2" sx={{fontWeight: "bold"}}>
              {movie.title}
            </Typography>
            <MovieLabels movie={movie}/>
          </Stack>
          <Typography sx={{
            color: "text.secondary",
            maxWidth: "500px",
          }}>{movie.plot}</Typography>
          <Stack spacing="16px" direction="row">
            {/* Play — navigates to the player route */}
            <Button
                variant="contained"
                onClick={handlePlay}
                sx={{
                  textTransform: "none",
                  fontWeight: "bold",
                  p: "12px 32px",
                  borderRadius: "12px",
                }}
            >
              <PlayArrowIcon sx={{mr: 0.5}}/>
              {HOME_PAGE_CONSTANTS.PLAY_BUTTON}
            </Button>

            <Button
                variant="outlined"
                sx={{
                  textTransform: "none",
                  fontWeight: "bold",
                  p: "12px 32px",
                  color: "text.primary",
                  borderRadius: "12px",
                }}
            >
              <AddIcon/>
              {HOME_PAGE_CONSTANTS.MY_LIST_BUTTON}
            </Button>
          </Stack>
        </Stack>
      </Box>
  )
}

type MovieLabelProps = {
  movie: MovieSummary;
}

function MovieLabels({movie}: MovieLabelProps) {

  return (
      <Stack direction="row" spacing="16px" sx={{alignItems: "center"}}>
        <Typography variant="body2"
                    sx={{color: "#CCC3D8"}}>{movie.releaseDate.split('-')[0]}</Typography>
        <Chip label={HOME_PAGE_CONSTANTS.ULTRA_HD_CHIP} size="small" sx={{
          borderRadius: "2px",
          backgroundColor: "text.disabled",
          color: "#CCC3D8"
        }}/>
        <Typography variant="body2"
                    sx={{color: "#CCC3D8"}}>{`${movie.durationInMinutes} min`}</Typography>
        <Typography variant="body2" sx={{color: "#CCC3D8"}}>{`${movie.genres[0].name}`}</Typography>
      </Stack>
  )
}