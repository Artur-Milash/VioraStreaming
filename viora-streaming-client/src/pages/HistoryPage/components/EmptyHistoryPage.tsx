import {Box, Button, Stack, Typography} from "@mui/material";
import HistoryToggleOffIcon from '@mui/icons-material/HistoryToggleOff';
import {HISTORY_PAGE_CONSTANTS} from "../../../constants/historyPageConstants.ts";

type EmptyHistoryPageProps = {
  onBrowse: () => void;
}

export function EmptyHistoryPage({onBrowse}: EmptyHistoryPageProps) {
  return (
      <Stack
          spacing="24px"
          sx={{
            minHeight: "400px",
            textAlign: "center",
            mb: '60px',
            alignItems: "center",
            justifyContent: "center"
          }}
      >
        <Box
            sx={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              width: "96px",
              height: "96px",
              mb: "20px",
              bgcolor: "background.paper",
              borderRadius: "100%",
            }}
        >
          <HistoryToggleOffIcon fontSize="large" sx={{color: "#CCC3D8"}} />
        </Box>
        <Typography variant="h5" sx={{fontWeight: 800, fontSize: "36px"}}>
          {HISTORY_PAGE_CONSTANTS.EMPTY_TITLE}
        </Typography>
        <Typography variant="body1" color="text.secondary"
                    sx={{maxWidth: "500px", fontSize: "16px", paddingBottom: "40px"}}>
          {HISTORY_PAGE_CONSTANTS.EMPTY_DESCRIPTION}
        </Typography>
        <Button
            variant="contained"
            color="primary"
            sx={{
              textTransform: "none",
              borderRadius: "12px",
              p: "12px 24px",
              fontWeight: "bold",
            }}
            onClick={() => onBrowse()}
        >
          {HISTORY_PAGE_CONSTANTS.EMPTY_BROWSE_BUTTON}
        </Button>
      </Stack>
  )
}