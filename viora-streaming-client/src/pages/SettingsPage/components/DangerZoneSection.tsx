import {Box, Button, Container, Stack, Typography} from "@mui/material";
import {DANGER_ZONE_DESCRIPTION, SETTINGS_UI} from "../../../constants/settingsConstants.ts";

type DangerZoneSectionProps = {
  onClick: () => void;
};

export function DangerZoneSection({onClick}: DangerZoneSectionProps) {
  return (
      <Container sx={{
        p: "32px",
        border: "1px solid",
        borderColor: "error.main",
        borderRadius: "12px",
      }}>
        <Stack sx={{justifyContent: "space-between", width: "100%"}} direction="row">
          <Box>
            <Typography variant="h6" sx={{
              color: "error.main",
              fontWeight: "bold",
            }}>{SETTINGS_UI.DANGER_ZONE_TITLE}</Typography>
            <Typography variant="body2" sx={{color: "#CCC3D8"}} className="">
              {DANGER_ZONE_DESCRIPTION}
            </Typography>
          </Box>
          <Button variant="outlined" color="error" sx={{
            textTransform: "none",
            fontWeight: "bold",
            borderRadius: "12px",
          }} onClick={onClick}>
            {SETTINGS_UI.DELETE_ACCOUNT_BUTTON}
          </Button>
        </Stack>
      </Container>
  )
}