import {Container, Grid, Stack, Typography} from "@mui/material";
import {ASSISTANT_DESCRIPTION, MOODS} from "../../../constants/assistantConstants.ts";
import {VioraButton} from "../../../components/Button/VioraButton.tsx";
import {useState} from "react";
import {MoodCard} from "./MoodCard.tsx";
import type {Mood} from "../../../types/moodTypes.ts";

type ChooseMoodSectionProps = {
  isLoading?: boolean;
  onFindMovies: (mood: Mood) => void;
};

export function ChooseMoodSection({onFindMovies, isLoading = false}: ChooseMoodSectionProps) {
  const [selectedMood, setSelectedMood] = useState<Mood>(MOODS[0]);


  return (
      <Container maxWidth="md" sx={{
        display: "flex",
        flexDirection: "column",
        gap: "48px"
      }}>
        <Stack spacing="8px">
          <Typography variant="h3" sx={{
            fontWeight: "bold",
          }}>What's your mood?</Typography>
          <Typography variant="body1" sx={{
            color: "#CCC3D8",
            maxWidth: "576px",
          }}>
            {ASSISTANT_DESCRIPTION}
          </Typography>
        </Stack>
        <Grid container spacing={2}>
          {MOODS.map((mood) => (
              <Grid item key={mood.name} size={{xs: 12, sm: 6, md: 4, lg: 3}}>
                <MoodCard
                    mood={mood}
                    isSelected={mood.name === selectedMood.name}
                    onClick={() => setSelectedMood(mood)}/>
              </Grid>
          ))}
        </Grid>


        <Stack sx={{alignItems: "center"}}>
          <VioraButton
              name={"Find my movies"}
              sx={{maxWidth: "185px"}}
              onClick={() => onFindMovies(selectedMood)}
              loading={isLoading}
          />
        </Stack>
      </Container>
  )
}