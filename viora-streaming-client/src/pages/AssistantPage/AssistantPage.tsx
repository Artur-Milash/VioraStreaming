import {Container} from "@mui/material";
import {ChooseMoodSection} from "./components/ChooseMoodSection.tsx";
import {RecommendationSection} from "./components/RecommendationSection.tsx";
import {useMoodMovies} from "../../hooks/useMoodMovies.ts";

export function AssistantPage() {
  const {suggestions, isLoading, findMovies} = useMoodMovies();

  return (
      <>
        <Container maxWidth="xl" sx={{
          display: "flex",
          flexDirection: "column",
          gap: "64px",
          py: "64px"
        }}>
          <ChooseMoodSection onFindMovies={mood => findMovies(mood.name)} isLoading={isLoading}/>

          {suggestions.length > 0 && <RecommendationSection suggestions={suggestions}/>}
        </Container>
      </>
  )
}