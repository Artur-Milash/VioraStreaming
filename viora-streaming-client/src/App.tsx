import {StrictMode} from "react";
import {ThemeProvider} from "@mui/material/styles";
import {vioraTheme} from "./vioraTheme.ts";
import {CssBaseline} from "@mui/material";
import {createBrowserRouter} from "react-router";
import {routes} from "./router.tsx";
import {AppLoader} from "./AppLoader.tsx";
import {Provider} from "react-redux";
import {store} from "./store/store.ts";
import {QueryClientProvider} from "@tanstack/react-query";
import {queryClient} from "./queryClient.ts";
import ModalContainer from "./components/Modals/ModalContainer.tsx";
import {Toast} from "./components/Toast/Toast.tsx";


const router = createBrowserRouter(routes);

export function App() {
  return (
      <>
        <StrictMode>
          <ThemeProvider theme={vioraTheme}>
            <CssBaseline/>
            <Provider store={store}>
              <QueryClientProvider client={queryClient}>
                <AppLoader router={router}/>
                <ModalContainer/>
                <Toast/>
              </QueryClientProvider>
            </Provider>
          </ThemeProvider>
        </StrictMode>
      </>
  )
}

export default App
