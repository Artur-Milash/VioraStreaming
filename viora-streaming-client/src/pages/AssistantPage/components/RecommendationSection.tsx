import {Box, Button, Chip, Container, Grid, Stack, Typography} from "@mui/material";
import type {MoodMovieSuggestion} from "../../../types/movieTypes.ts";
import {useNavigate} from "react-router-dom";
import {API_PAGE} from "../../../constants/routingConstants.ts";

type RecommendationSectionProps = {
  suggestions: MoodMovieSuggestion[];
}

export function RecommendationSection({suggestions}: RecommendationSectionProps) {
  const [first, ...rest] = suggestions;

  return (
      <Container maxWidth="lg">
        <Stack spacing={1}>
          <Typography variant="body2" color="primary" sx={{
            letterSpacing: "2.4px",
            fontWeight: "bold"
          }}>
            THE NEURAL SELECTION
          </Typography>
          <Typography variant="h4" sx={{
            fontWeight: "bold"
          }}>
            AI Recommendations
          </Typography>
        </Stack>

        <Grid container spacing={2} sx={{mt: "40px"}}>
          {first && (
              <Grid size={{xs: 12, lg: 8}}>
                <MostMatchedMovie suggestion={first}/>
              </Grid>
          )}

          {rest.map((suggestion, index) => (
              <Grid key={suggestion.movie.id ?? index} size={{xs: 12, sm: 6, lg: 4}}>
                <MatchedMovie suggestion={suggestion}/>
              </Grid>
          ))}
        </Grid>
      </Container>
  )
}


function MostMatchedMovie({suggestion}: { suggestion: MoodMovieSuggestion }) {
  const {movie, matchLabel} = suggestion;
  const navigate = useNavigate();

  return (
      <Box sx={{
        height: "400px",
        p: "20px",
        position: "relative"
      }}>
        <Box
            component="img"
            src={movie.poster}
            alt={movie.title}
            sx={{
              position: "absolute",
              inset: 0,
              width: "100%",
              height: "100%",
              objectFit: "cover",
              objectPosition: "top center",
              filter: "grayscale(100%)",
              pointerEvents: "none",
            }}
        />
        <Stack sx={{
          height: "100%",
          justifyContent: "space-between",
          position: "relative",
          zIndex: 1,
        }}>
          <Stack direction="row">
            <Chip label={matchLabel} color="primary" size="small" sx={{
              fontWeight: "bold",
              textTransform: "uppercase",
              backgroundColor: "primary.main",
              color: "text.primary",
              fontSize: "10px",
              letterSpacing: "1px",
            }}/>
          </Stack>
          <Box>
            <Stack direction="row" spacing="8px">
              {movie.genres.map(genre => (
                  <Chip key={genre.id} label={genre.name} sx={{
                    backgroundColor: "rgba(53,52,55,0.6)",
                    borderRadius: "4px",
                    p: "2px 4px",
                    textTransform: "uppercase",
                    fontWeight: "bold",
                    fontSize: "10px",
                  }}/>
              ))}
            </Stack>
            <Stack sx={{mt: "10px"}}>
              <Typography variant="h4" sx={{
                fontWeight: "bold",
                color: "text.primary",
                textTransform: "uppercase",
              }}>{movie.title}</Typography>
              <Typography variant="body2" sx={{
                maxWidth: "450px",
                color: "#CCC3D8",
                maxHeight: "100px",
                overflow: "hidden",
                textOverflow: "ellipsis",
              }}>
                {movie.plot}
              </Typography>
            </Stack>

            <Stack direction="row" spacing="16px" sx={{mt: "20px"}}>
              <Button variant="contained" sx={{
                backgroundColor: "#fff",
                textTransform: "none",
                fontWeight: "bold",
                borderRadius: "8px",
                p: "10px 24px",
                color: "#000"
              }}
                      onClick={() => navigate(`/${API_PAGE.Movies}/${movie.id}/player`)}
              >
                Watch Now
              </Button>
              <Button variant="outlined" sx={{
                borderColor: "rgba(255,255,255,0.1)",
                textTransform: "none",
                fontWeight: "bold",
                borderRadius: "8px",
                p: "10px 24px",
                color: "#fff"
              }}
                      onClick={() => navigate(`${API_PAGE.Movies}/${movie.id}`)}
              >
                Details
              </Button>
            </Stack>
          </Box>
        </Stack>
      </Box>
  )
}

function MatchedMovie({suggestion}: { suggestion: MoodMovieSuggestion }) {
  const navigate = useNavigate();

  const {movie, matchLabel} = suggestion;
  const releaseYear = movie.releaseDate ? new Date(movie.releaseDate).getFullYear() : "";
  const hours = Math.floor(movie.durationInMinutes / 60);
  const minutes = movie.durationInMinutes % 60;
  const duration = `${hours}h ${minutes}m`;

  return (
      <Box sx={{
        height: "400px",
        p: "20px",
        position: "relative",
        '&:hover .overlay': {opacity: 1, pointerEvents: 'auto'},
        '&:hover .movie-summary': {opacity: 0},
      }}>
        <Box sx={{
          position: "absolute",
          inset: 0,
          backgroundImage: `linear-gradient(rgba(0,0,0,0.6), rgba(0,0,0,0.6)), url(${movie.poster})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          filter: "grayscale(100%)",
        }}/>
        <Stack sx={{
          height: "100%",
          justifyContent: "space-between",
          position: "relative",
          zIndex: 1,
        }}>
          <Stack direction="row">
            <Chip label={matchLabel} color="primary" size="small" sx={{
              fontWeight: "bold",
              textTransform: "uppercase",
              backgroundColor: "primary.main",
              color: "text.primary",
              fontSize: "10px",
              letterSpacing: "1px",
            }}/>
          </Stack>
          <Box className="movie-summary">
            <Stack sx={{mt: "10px"}}>
              <Typography variant="h5" sx={{
                fontWeight: "bold",
                color: "text.primary",
                textTransform: "uppercase",
              }}>{movie.title}</Typography>
              <Typography variant="body2" sx={{maxWidth: "450px", color: "text.secondary"}}>
                {matchLabel}
              </Typography>
            </Stack>
          </Box>
        </Stack>
        <Box className="overlay" sx={{
          position: "absolute",
          inset: 0,
          opacity: 0,
          transition: "opacity 0.3s ease",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          p: "20px",
          pointerEvents: "none",
          zIndex: 2,
        }}>
          <Stack sx={{
            width: "100%",
            justifyContent: "space-between",
            height: "100%",
            pt: "40px",
          }}>
            <Stack spacing={1}>
              <Typography variant="h6" sx={{fontWeight: "bold"}}>
                {movie.title}
              </Typography>
              <Typography variant="body2" sx={{
                color: "#CCC3D8",
                maxHeight: "60px",
                overflow: "hidden",
                textOverflow: "ellipsis",
              }}>
                {movie.plot}
              </Typography>
            </Stack>

            <Box>
              <Stack
                  direction="row"
                  spacing="8px"
                  divider={<Typography variant="body2"
                                       sx={{color: "text.secondary"}}>·</Typography>}
              >
                <Typography variant="body2" sx={{
                  color: "text.secondary",
                  fontWeight: "bold"
                }}>{releaseYear}</Typography>
                <Typography variant="body2" sx={{
                  color: "text.secondary",
                  fontWeight: "bold"
                }}>{movie.genres[0]?.name}</Typography>
                <Typography variant="body2" sx={{
                  color: "text.secondary",
                  fontWeight: "bold"
                }}>{duration}</Typography>
              </Stack>

              <Button
                  variant="contained"
                  fullWidth
                  sx={{
                    backgroundColor: "#353437",
                    textTransform: "none",
                    fontWeight: "bold",
                    borderRadius: "8px",
                    p: "10px 24px",
                    color: "#fff",
                    mt: "20px",
                  }}
                  onClick={() => navigate(`${API_PAGE.Movies}/${movie.id}`)}
              >
                Details
              </Button>
            </Box>
          </Stack>
        </Box>
      </Box>
  )
}