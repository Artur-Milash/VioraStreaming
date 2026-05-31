import {Stack, Typography} from "@mui/material";
import GroupsIcon from '@mui/icons-material/Groups';
import {HOME_PAGE_CONSTANTS} from "../../../constants/homePageConstants.ts";

export function JoinTheCommunity() {
  return (
      <Stack sx={{
        p: "32px",
        bgcolor: "primary.main",
        borderRadius: "12px",
        minHeight: "400px",
        width: "100%",
        cursor: "pointer",
        justifyContent: "space-between",
      }}>

        <Typography variant="h4" sx={{color: "white", fontWeight: "bolder"}}>
          {HOME_PAGE_CONSTANTS.JOIN_COMMUNITY_TITLE}
        </Typography>

        <Typography variant="body1" sx={{fontWeight: "bold", maxWidth: "190px"}}>
          {HOME_PAGE_CONSTANTS.JOIN_COMMUNITY_SUBTITLE}
        </Typography>

        <Stack direction="row" sx={{justifyContent: "end"}}>
          <GroupsIcon fontSize="large" />
        </Stack>
      </Stack>
  )
}