import {Header} from "../Header/Header.tsx";
import {Outlet} from "react-router-dom";
import {Box, Stack} from "@mui/material";
import {SidebarPanel} from "../Sidebar/SidebarPanel.tsx";
import ProtectedRoute from "../../router/ProtectedRoute.tsx";

export function AppLayoutWithSideNav() {
  return (
      <>
        <ProtectedRoute>
          <Header/>
          <Stack direction="row">
            <SidebarPanel/>
            <Box sx={{flex: 1}}>
              <Outlet/>
            </Box>
          </Stack>
        </ProtectedRoute>
      </>
  );

}