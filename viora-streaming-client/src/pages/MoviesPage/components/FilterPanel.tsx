import {Box, Typography, Stack, Checkbox, FormControlLabel, Slider, Button} from "@mui/material";
import {VioraButton} from "../../../components/Button/VioraButton.tsx";
import {
  DEFAULT_DURATION,
  LIST_OF_DURATION,
  MAX_DURATION
} from "../../../constants/filterConstants.ts";
import {useFilterPanel} from "../../../hooks/useFilterPanel.ts";
import {GENRES} from "../../../constants/genres.ts";
import {useState} from "react";
import {MOVIES_PAGE_CONSTANTS} from "../../../constants/moviesPageConstants.ts";


export function FilterPanel() {
  const {
    genres, rating, releaseYear, committedRating, committedReleaseYear, duration,
    handleGenreChange, handleRatingChange, handleRatingCommit,
    handleReleaseYearChange, handleReleaseYearCommit, handleDurationChange, handleReset,
  } = useFilterPanel();

  return (
      <Box sx={{
        width: "280px",
        p: "32px 24px",
        backgroundColor: "background.paper",
        display: "flex",
        flexDirection: "column",
        justifyContent: "space-between"
      }}>
        <Box>
          <Typography variant="h6" sx={{fontWeight: 700, mb: "32px", fontSize: "20px"}}>
            {MOVIES_PAGE_CONSTANTS.FILTER_TITLE}
          </Typography>

          {/* GENRES */}
          <Genres selectedGenres={genres} handleGenreChange={handleGenreChange}/>

          {/* RATING */}
          <Stack direction="row" sx={{mb: "16px", justifyContent: "space-between"}}>
            <Typography variant="body2" color="text.secondary"
                        sx={{textTransform: "uppercase", fontWeight: 700}}>{MOVIES_PAGE_CONSTANTS.FILTER_RATING_LABEL}</Typography>
            <Typography variant="body2" color="primary.main"
                        sx={{fontWeight: 700}}>{committedRating.toFixed(1)}+</Typography>
          </Stack>
          <Slider
              value={rating}
              onChange={handleRatingChange}
              onChangeCommitted={handleRatingCommit}
              min={0} max={10} step={0.1} valueLabelDisplay="auto"
              sx={{
                mb: "24px",
                color: "primary.main",
                "& .MuiSlider-rail": {backgroundColor: "secondary.main"}
              }}
          />

          {/* RELEASE YEAR */}
          <Stack direction="row" sx={{mb: "16px"}}>
            <Typography variant="body2" color="text.secondary"
                        sx={{textTransform: "uppercase", fontWeight: 700}}>{MOVIES_PAGE_CONSTANTS.FILTER_RELEASE_YEAR_LABEL}</Typography>
            <Typography variant="body2" color="primary.main"
                        sx={{fontWeight: 700, textAlign: "right", paddingLeft: "50px"}}>
              {committedReleaseYear[0]} -<br/> {committedReleaseYear[1]}
            </Typography>
          </Stack>
          <Slider
              value={releaseYear}
              onChange={handleReleaseYearChange}
              onChangeCommitted={handleReleaseYearCommit}
              min={1990} max={new Date().getFullYear()} step={1} valueLabelDisplay="auto"
              sx={{mb: "24px", color: "primary.main"}}
          />

          {/* DURATION */}
          <Typography variant="body2" color="text.secondary" sx={{
            textTransform: "uppercase",
            fontWeight: 700,
            mb: "16px"
          }}>{MOVIES_PAGE_CONSTANTS.FILTER_DURATION_LABEL}</Typography>
          <Stack direction="row" spacing="8px" sx={{mb: "32px"}}>
            {LIST_OF_DURATION.map((option) => (
                <Button
                    key={option}
                    variant={duration === option ? "contained" : "outlined"}
                    onClick={() => handleDurationChange(option)}
                    sx={{
                      flex: 1, textTransform: "none", borderRadius: "12px",
                      borderColor: duration === option ? "primary.main" : "secondary.main",
                      backgroundColor: duration === option ? "primary.main" : "secondary.main",
                      color: duration === option ? "text.primary" : "text.secondary",
                      "&:hover": {
                        backgroundColor: duration === option ? "primary.main" : "secondary.main",
                        opacity: 0.9
                      },
                    }}
                >
                  <Typography variant="caption" sx={{fontWeight: "bold"}}>
                    {option === MAX_DURATION ? `${MAX_DURATION} min` : option === DEFAULT_DURATION ? `${DEFAULT_DURATION} min` : option}
                  </Typography>
                </Button>
            ))}
          </Stack>

          <VioraButton name={MOVIES_PAGE_CONSTANTS.FILTER_RESET_BUTTON} onClick={handleReset}/>
        </Box>
      </Box>
  );
}

interface GenreProps {
  selectedGenres: number[];
  handleGenreChange: (genre: number) => void;
}

function Genres({selectedGenres, handleGenreChange}: GenreProps) {
  const [showAll, setShowAll] = useState(false);

  const visibleGenres = showAll ? GENRES : GENRES.slice(0, 4);

  return (
      <>
        <Typography variant="body2" color="text.secondary" sx={{
          textTransform: "uppercase", fontWeight: 700, mb: "16px", fontSize: "12px"
        }}>
          {MOVIES_PAGE_CONSTANTS.FILTER_GENRES_LABEL}
        </Typography>

        <Box sx={{
          maxHeight: showAll ? "260px" : "none",
          overflowY: showAll ? "auto" : "visible",
          pr: showAll ? "4px" : 0,
          mb: "8px",
          "&::-webkit-scrollbar": {width: "4px"},
          "&::-webkit-scrollbar-track": {backgroundColor: "transparent"},
          "&::-webkit-scrollbar-thumb": {backgroundColor: "secondary.main", borderRadius: "4px"},
        }}>
          <Stack spacing="8px" sx={{mb: "8px"}}>
            {visibleGenres.map((genre) => (
                <FormControlLabel
                    key={genre.id}
                    control={
                      <Checkbox
                          checked={selectedGenres.includes(genre.id)}
                          onChange={() => handleGenreChange(genre.id)}
                          sx={{color: "text.secondary", "&.Mui-checked": {color: "primary.main"}}}
                      />
                    }
                    label={
                      <Typography variant="body2" sx={{
                        color: selectedGenres.includes(genre.id) ? "text.primary" : "text.secondary"
                      }}>
                        {genre.name}
                      </Typography>
                    }
                />
            ))}
          </Stack>
        </Box>

        <Button
            variant="text"
            color="primary"
            sx={{textTransform: "none", mb: "24px", p: 0}}
            onClick={() => setShowAll((prev) => !prev)}
        >
          {showAll ? MOVIES_PAGE_CONSTANTS.FILTER_SHOW_LESS : MOVIES_PAGE_CONSTANTS.FILTER_SHOW_MORE}
        </Button>
      </>
  );
}