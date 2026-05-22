import {AppBar, Toolbar, Stack, Avatar, Container, Box} from "@mui/material";
import {VioraLogo} from "../Logo/VioraLogo.tsx";
import {HeaderNavigation} from "./subcomponents/HeaderNavigation.tsx";
import {SearchField} from "./subcomponents/SearchField.tsx";
import userAvatar from "../../assets/user-acc-img.jpg";
import {useDispatch} from "react-redux";
import type {AppDispatch} from "../../store/store.ts";
import {setTitle} from "../../store/filterSlice.ts";
import {useNavigate} from "react-router-dom";
import {API_PAGE} from "../../constants/routingConstants.ts";

export function Header() {

  const dispatch = useDispatch<AppDispatch>();
  const navigate = useNavigate();

  return (
      <AppBar
          position="sticky"
          elevation={0}
          sx={{
            backgroundColor: "background.default",
            borderBottom: "1px solid",
            borderColor: "secondary.main",
          }}
      >
        <Container maxWidth={false}>
          <Toolbar
              disableGutters
              sx={{
                height: "80px",
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                position: "relative",
              }}
          >
            <Stack direction="row" spacing="48px">
              <VioraLogo variant="h4" fontSize="24px"/>
              <HeaderNavigation/>
            </Stack>

            <Box
                sx={{
                  position: "absolute",
                  left: "60%",
                  transform: "translateX(-50%)",
                  width: "700px",
                  display: "flex",
                  justifyContent: "center",
                  pointerEvents: "none",
                  "& > *": {pointerEvents: "auto"}
                }}
            >
              <SearchField onSearch={(searchQuery: string) => dispatch(setTitle(searchQuery))}/>
            </Box>

            <Stack direction="row">
              <Avatar
                  src={userAvatar}
                  onClick={() => navigate(API_PAGE.Settings)}
                  sx={{
                    width: 40,
                    height: 40,
                    cursor: "pointer",
                    transition: "transform 0.2s",
                    "&:hover": {transform: "scale(1.1)"}
                  }}
              />
            </Stack>
          </Toolbar>
        </Container>
      </AppBar>
  );
}