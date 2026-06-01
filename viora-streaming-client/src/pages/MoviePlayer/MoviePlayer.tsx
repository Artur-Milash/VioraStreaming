import {useParams, useNavigate} from "react-router";
import {usePlayer} from "../../hooks/usePlayer.ts";
import {Player} from "./components/Player.tsx";
import {Box, CircularProgress} from "@mui/material";
import {API_BASE} from "../../utils/apiUtils.ts";

export function MoviePlayer() {
  const {id} = useParams<{ id: string }>();
  const navigate = useNavigate();

  const {title, isLoading, history, videoUrl} = usePlayer(Number(id));

  if (isLoading) {
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

  if (!videoUrl) {
    navigate('/error');
    return null;
  }

  return (
      <Player
          videoUrl={videoUrl}
          dbMovieId={Number(id)}
          title={title}
          onClose={() => navigate(-1)}
          startFrom={history?.lastWatchedAt}
          apiBaseUrl={API_BASE}
      />
  );
}