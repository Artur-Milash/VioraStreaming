import {Stack, Typography} from "@mui/material";
import GroupsIcon from '@mui/icons-material/Groups';

export function JoinTheCommunity() {
  return (
      <Stack sx={{
        p: "32px",
        bgcolor: "primary.main",
        borderRadius: "12px",
        minHeight: "400px",
        width: "100%",
        justifyContent: "space-between",
      }}>
        <Typography variant="h4" sx={{color: "white", fontWeight: "bolder"}}>
          Join the community
        </Typography>
        <Typography variant="body1" sx={{fontWeight: "bold", maxWidth: "190px"}}>
          Connect with millions of Viora viewers and share your lists
        </Typography>
        <Stack direction="row" sx={{justifyContent: "end"}}>
          <GroupsIcon fontSize="large" />
        </Stack>
      </Stack>
  )
}