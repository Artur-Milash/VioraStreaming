import {Link, Stack} from "@mui/material";
import {NavLink} from "react-router-dom";
import {API_PAGE} from "../../../constants/routingConstants.ts";
import {NAVIGATION_CONSTANTS} from "../../../constants/navigationConstants.ts";

const navItems = [
  {name: NAVIGATION_CONSTANTS.HOME, path: API_PAGE.Home},
  {name: NAVIGATION_CONSTANTS.MOVIES, path: API_PAGE.Movies},
  {name: NAVIGATION_CONSTANTS.HISTORY, path: API_PAGE.History},
];

export function HeaderNavigation() {
  return (
      <Stack direction="row" spacing="32px">
        {navItems.map((item) => (
            <Link
                key={item.name}
                component={NavLink}
                to={item.path}
                underline="none"
                variant="body1"
                sx={{
                  color: "text.primary",
                  fontWeight: 500,
                  fontSize: "16px",
                  textTransform: "none",
                  position: "relative",
                  pb: "8px",

                  "&::after": {
                    content: '""',
                    position: "absolute",
                    bottom: 0,
                    left: 0,
                    width: "100%",
                    height: "2px",
                    borderRadius: "2px",
                    backgroundColor: "primary.main",
                    transform: "scaleX(0)",
                    opacity: 0,
                    transition: "all 0.2s ease",
                  },

                  "&:hover::after": {
                    transform: "scaleX(1)",
                    opacity: 1,
                  },

                  "&.active::after": {
                    transform: "scaleX(1)",
                    opacity: 1,
                  },

                  ".nav-container:hover &.active:not(:hover)::after": {
                    transform: "scaleX(0)",
                    opacity: 0,
                  },
                }}
            >
              {item.name}
            </Link>
        ))}
      </Stack>
  );
}