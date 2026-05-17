import {Box, Card, Container, Grid, Stack, Typography} from "@mui/material";
import {ASSISTANT_DESCRIPTION, MOODS} from "../../constants/assistantConstants.ts";
import {VioraButton} from "../../components/Button/VioraButton.tsx";
import {ChooseMoodSection} from "./components/ChooseMoodSection.tsx";

export function AssistantPage() {
  return (
      <>
        <Container maxWidth="xl" sx={{pt: "64px"}}>
          <ChooseMoodSection onFindMovies={mood => {}} isLoading={false}/>


        </Container>
      </>
  )
}