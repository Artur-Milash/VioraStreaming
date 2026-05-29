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
                alignItems: "center",
                justifyContent: "space-between", // Розкидає елементи по краях
              }}
          >
            {/* 1. ЛІВА ЧАСТИНА: Логотип + Меню */}
            {/* flexShrink: 0 гарантує, що меню не буде стискатися і ламатися */}
            <Stack
                direction="row"
                spacing={6}
                sx={{
                  flexShrink: 0,
                  alignItems: "center" // Перенесли сюди
                }}
            >
              <VioraLogo variant="h4" fontSize="24px"/>
              <HeaderNavigation/>
            </Stack>

            {/* 2. ЦЕНТРАЛЬНА ЧАСТИНА: Розумний контейнер для пошуку */}
            <Box
                sx={{
                  flexGrow: 1, // Дозволяє зайняти весь вільний простір між меню та аватаром
                  display: { xs: "none", md: "flex" }, // Ховаємо на мобільних
                  justifyContent: "center",
                  px: 4, // Гарантовані безпечні відступи з обох боків, щоб ніколи не налізти на текст
                }}
            >
              {/* Сам пошук, який має максимальні ліміти ширини, щоб не бути надто довгим на 4K */}
              <Box sx={{
                width: "100%",
                maxWidth: { md: "350px", lg: "550px", xl: "700px" },
                transition: "max-width 0.2s ease"
              }}>
                <SearchField onSearch={(searchQuery: string) => dispatch(setTitle(searchQuery))}/>
              </Box>
            </Box>

            {/* 3. ПРАВА ЧАСТИНА: Аватар */}
            <Stack direction="row" sx={{ flexShrink: 0 }}>
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