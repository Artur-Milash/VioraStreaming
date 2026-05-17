import {Box, Card, CardActionArea, Stack, Typography} from "@mui/material";
import type {Mood} from "../../../types/moodTypes.ts";

type MoodCardProps = {
  mood: Mood,
  isSelected: boolean,
  onClick: () => void,
}

export function MoodCard({mood, isSelected, onClick}: MoodCardProps) {
  return (
      <Card sx={{
        background: 'none',
        boxShadow: 'none',
      }}>
        <CardActionArea
            onClick={onClick}
            data-active={isSelected || undefined}
            sx={{
              p: "20px",
              borderRadius: "12px",
              border: "1px solid #6B7280",
              '&[data-active]': {
                borderColor: "#7C3AED",
              }
            }}
        >
          <Stack spacing="15px">
            <Typography variant="h4">{mood.emoji}</Typography>
            <Box>
              <Typography variant="h6" sx={{
                fontWeight: "bold"
              }}>{mood.name}</Typography>
              <Typography variant="body2" sx={{
                color: "#A1A1AA"
              }}>{mood.description}</Typography>
            </Box>
          </Stack>
        </CardActionArea>
      </Card>
  )
}