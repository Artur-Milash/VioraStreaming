import { Stack, Typography, Chip, Button } from "@mui/material";
import ClearIcon from "@mui/icons-material/Clear";
import { setGenres, setRating, resetFilters } from "../../../store/filterSlice.ts";
import { DEFAULT_RATING } from "../../../constants/filterConstants.ts";
import {useDispatch, useSelector} from "react-redux";
import type {AppDispatch, RootState} from "../../../store/store.ts";
import {GENRES} from "../../../constants/genres.ts";
import {MOVIES_PAGE_CONSTANTS} from "../../../constants/moviesPageConstants.ts";

export function ActiveFilters() {
  const dispatch = useDispatch<AppDispatch>();
  const {genres, rating} = useSelector((state: RootState) => state.filters)

  const hasActiveRating = rating > DEFAULT_RATING;
  const hasAnything = genres.length > 0 || hasActiveRating;

  if (!hasAnything) return null;

  const chipSx = {
    backgroundColor: "secondary.main",
    color: "text.primary",
    borderRadius: "8px",
    "& .MuiChip-deleteIcon": { color: "text.primary" },
  };

  const deleteIcon = <ClearIcon style={{ color: "white", fontSize: "16px" }} />;

  return (
      <Stack direction="row" spacing="12px" sx={{ mb: "40px", alignItems: "center", flexWrap: "wrap" }}>
        <Typography variant="body2" color="text.secondary">
          {MOVIES_PAGE_CONSTANTS.ACTIVE_FILTERS_LABEL}
        </Typography>

        {genres.map((genre) => (
            <Chip
                key={genre}
                label={GENRES.find(g => g.id === genre)?.name}
                deleteIcon={deleteIcon}
                onDelete={() => dispatch(setGenres(genres.filter((g) => g !== genre)))}
                sx={chipSx}
            />
        ))}

        {hasActiveRating && (
            <Chip
                label={`Rating: ${rating.toFixed(1)}+`}
                deleteIcon={deleteIcon}
                onDelete={() => dispatch(setRating(DEFAULT_RATING))}
                sx={chipSx}
            />
        )}

        <Button
            variant="text"
            color="primary"
            sx={{ textTransform: "none", p: 0 }}
            onClick={() => dispatch(resetFilters())}
        >
          {MOVIES_PAGE_CONSTANTS.CLEAR_ALL_BUTTON}
        </Button>
      </Stack>
  );
}