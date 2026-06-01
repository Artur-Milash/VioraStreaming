import {SETTINGS_PAGE_DESCRIPTION, SETTINGS_UI} from "../../../constants/settingsConstants.ts";
import {Container, Typography} from "@mui/material";

export function SettingHeaderSection() {
  return (
      <Container>
        <Typography variant="h4" sx={{fontWeight: "bold"}}>{SETTINGS_UI.PAGE_TITLE}</Typography>
        <Typography sx={{color: "#CCC3D8"}}>{SETTINGS_PAGE_DESCRIPTION}</Typography>
      </Container>
  )
}