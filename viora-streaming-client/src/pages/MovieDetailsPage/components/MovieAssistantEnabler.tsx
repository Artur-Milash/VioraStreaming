import {Box, Button, Stack, Typography} from "@mui/material";
import SmartToyOutlinedIcon from '@mui/icons-material/SmartToyOutlined';
import {MOVIE_DETAILS_PAGE_CONSTANTS} from "../../../constants/movieDetailsPageConstants.ts";

type MovieAssistantEnablerProps = {
  content: string;
  onButtonClick: () => void;
}

export function MovieAssistantEnabler({content, onButtonClick}: MovieAssistantEnablerProps) {
  return (
      <Stack spacing="16px" sx={{
        backgroundColor: "background.paper",
        p: "25px",
        maxWidth: "312px",
        borderRadius: "16px",
      }}>

        <Stack direction="row" spacing="10px" sx={{alignItems: "center"}}>
          <Box sx={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            borderRadius: "100%",
            backgroundColor: "primary.main",
            height: "28px",
            width: "28px"
          }}>
            <SmartToyOutlinedIcon fontSize="small"/>
          </Box>
          <Typography variant="body1" sx={{fontWeight: "bold"}}>
            {MOVIE_DETAILS_PAGE_CONSTANTS.ASSISTANT_TITLE}
          </Typography>
        </Stack>

        <Typography variant="body2">
          {content ?? "Have questions about the complex timeline of \"The Architects\n" +
              "          of Time\"?\n" +
              "          Our AI can explain the paradoxes or help you find similar mind-bending\n" +
              "          sci-fi."}
        </Typography>

        <Button variant="contained" sx={{
          textTransform: "none",
          fontWeight: "bold",
          borderRadius: "8px",
        }} onClick={onButtonClick}>
          {MOVIE_DETAILS_PAGE_CONSTANTS.DISCUSS_BUTTON}
        </Button>

      </Stack>
  )
}