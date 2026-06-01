import type {MovieSummary} from "../../../types/movieTypes.ts";
import {Box, Stack, Typography} from "@mui/material";
import {HOME_PAGE_CONSTANTS} from "../../../constants/homePageConstants.ts";

type WeekendSpecialProps = {
  movie: MovieSummary;
  onClick?: () => void;
}

export function WeekendSpecial({movie, onClick}: WeekendSpecialProps) {
  return (
      <Box sx={{
        p: "32px",
        bgcolor: "background.paper",
        borderRadius: "12px",
        minHeight: "400px",
        width: "100%",
        cursor: "pointer",
      }} onClick={onClick}>
        <Stack spacing="10px" sx={{
          height: "100%",
          justifyContent: "end",
        }}>
          <Typography sx={{
            color: "primary.main",
            textTransform: "uppercase",
          }}>{HOME_PAGE_CONSTANTS.WEEKEND_SPECIAL_LABEL}</Typography>
          <Typography variant="h4" sx={{
            fontWeight: "bold",
          }}>
            {movie.title}
          </Typography>
          <Typography variant="body2" sx={{color: "text.secondary"}}>
            {movie.plot}
          </Typography>
        </Stack>
      </Box>
  )
}