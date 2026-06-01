import {isRouteErrorResponse, useNavigate, useRouteError} from 'react-router-dom';
import {Box, Stack, Typography} from '@mui/material';
import {VioraLogo} from '../../components/Logo/VioraLogo.tsx';
import {VioraButton} from '../../components/Button/VioraButton.tsx';
import {API_PAGE} from '../../constants/routingConstants.ts';
import {ERROR_MESSAGES, ERROR_PAGE_CONSTANTS} from '../../constants/errorPageConstants.ts';

function resolveError(routeError: unknown): { code: number; title: string; description: string } {
  if (isRouteErrorResponse(routeError)) {
    const known = ERROR_MESSAGES[routeError.status];
    return {
      code: routeError.status,
      title: known?.title ?? routeError.statusText ?? 'Error',
      description: known?.description ?? ERROR_PAGE_CONSTANTS.FALLBACK_DESCRIPTION,
    };
  }

  if (routeError instanceof Error) {
    return {
      code: 500,
      title: ERROR_PAGE_CONSTANTS.FALLBACK_TITLE,
      description: routeError.message || ERROR_PAGE_CONSTANTS.FALLBACK_DESCRIPTION,
    };
  }

  return {
    code: 404,
    ...ERROR_MESSAGES[404],
  };
}

export function ErrorPage() {
  const routeError = useRouteError();
  const navigate = useNavigate();
  const {code, title, description} = resolveError(routeError);

  return (
      <Box
          sx={{
            minHeight: '100vh',
            backgroundColor: 'background.default',
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            px: 4,
          }}
      >
        <Stack spacing={2} sx={{
          maxWidth: 480,
          width: '100%',
          textAlign: 'center',
          alignItems: "center"
        }}>
          <VioraLogo fontSize={32}/>

          <Typography
              sx={{
                fontSize: {xs: '7rem', sm: '11rem'},
                fontWeight: 800,
                color: 'primary.main',
                lineHeight: 1,
                mt: 1,
                letterSpacing: '-4px',
              }}
          >
            {code}
          </Typography>

          <Typography
              variant="h5"
              sx={{fontWeight: 700, color: 'text.primary'}}
          >
            {title}
          </Typography>

          <Typography
              variant="body1"
              sx={{color: 'text.secondary', lineHeight: 1.7}}
          >
            {description}
          </Typography>

          <VioraButton
              name={ERROR_PAGE_CONSTANTS.BACK_TO_HOME_BUTTON}
              type="button"
              onClick={() => navigate(API_PAGE.Home, {replace: true})}
              sx={{mt: 1}}
          />
        </Stack>
      </Box>
  );
}
