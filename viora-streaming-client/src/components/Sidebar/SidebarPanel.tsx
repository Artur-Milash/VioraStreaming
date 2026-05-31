import {Box, Stack, Typography} from "@mui/material";
import {API_PAGE} from "../../constants/routingConstants.ts";
import HomeFilledIcon from "@mui/icons-material/HomeFilled";
import MovieCreationOutlinedIcon from "@mui/icons-material/MovieCreationOutlined";
import BookmarkBorderOutlinedIcon from "@mui/icons-material/BookmarkBorderOutlined";
import AutoAwesomeOutlinedIcon from "@mui/icons-material/AutoAwesomeOutlined";
import SettingsIcon from '@mui/icons-material/Settings';
import {NavLink} from "react-router-dom";
import {NAVIGATION_CONSTANTS} from "../../constants/navigationConstants.ts";

const sidebarItems = [
  {name: NAVIGATION_CONSTANTS.HOME, path: API_PAGE.Home, icon: HomeFilledIcon},
  {name: NAVIGATION_CONSTANTS.MOVIES, path: API_PAGE.Movies, icon: MovieCreationOutlinedIcon},
  {name: NAVIGATION_CONSTANTS.HISTORY, path: API_PAGE.History, icon: BookmarkBorderOutlinedIcon},
  {name: NAVIGATION_CONSTANTS.SETTINGS, path: API_PAGE.Settings, icon: SettingsIcon},
  {name: NAVIGATION_CONSTANTS.AI_ASSISTANT, path: API_PAGE.Assistant, icon: AutoAwesomeOutlinedIcon},
];

export function SidebarPanel() {
  return (
      <Box
          sx={{
            width: "220px",
            flexShrink: 0,
            p: "16px",
            backgroundColor: "background.paper",
            display: "flex",
            flexDirection: "column",
            borderRight: "1px solid",
            borderColor: "secondary.main",
            minHeight: "100vh",
            borderRightWidth: "1px",
            borderRightStyle: "solid",
            borderRightColor: "#353437",
          }}
      >
        <Stack spacing="4px">
          {sidebarItems.map((item) => {
            const Icon = item.icon;
            return (
                <Box
                    key={item.name}
                    component={NavLink}
                    to={item.path}
                    sx={{
                      display: "flex",
                      textDecoration: "none",
                      borderRadius: "10px",
                      p: "12px 16px",
                      color: "text.secondary",
                      transition: "all 0.15s ease",
                      "&:hover": {
                        backgroundColor: "secondary.main",
                        color: "text.primary",
                      },
                      "&.active": {
                        backgroundColor: "primary.main",
                        color: "text.primary",
                      },
                    }}
                >
                  <Stack direction="row" spacing="16px" sx={{
                    alignItems: "center"
                  }}>
                    <Icon sx={{fontSize: 20}}/>
                    <Typography variant="body1" sx={{fontWeight: 500, color: "inherit"}}>
                      {item.name}
                    </Typography>
                  </Stack>
                </Box>
            );
          })}
        </Stack>
      </Box>
  );
}