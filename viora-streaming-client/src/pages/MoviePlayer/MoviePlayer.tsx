import {useParams, useNavigate} from "react-router";
import {usePlayer} from "../../hooks/usePlayer.ts";
import {Player} from "./components/Player.tsx";
import {Box, CircularProgress} from "@mui/material";
import {API_BASE} from "../../utils/apiUtils.ts";

export function MoviePlayer() {
  const {id} = useParams<{ id: string }>();
  const navigate = useNavigate();

  // useParams always returns string | undefined — parse to number for usePlayer
  const {imdbId, title, isLoading, history} = usePlayer(Number(id));

  if (isLoading || !imdbId) {
    return (
        <Box
            sx={{
              position: "fixed",
              inset: 0,
              bgcolor: "#000",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              zIndex: 1400,
            }}
        >
          <CircularProgress sx={{color: "#e50914"}} size={64} thickness={2}/>
        </Box>
    );
  }

  return (
      <Player
          movieId={imdbId}
          dbMovieId={Number(id)}
          apiBaseUrl={API_BASE}
          title={title}
          onClose={() => navigate(-1)}
          startFrom={history?.lastWatchedAt}
      />
  );
}