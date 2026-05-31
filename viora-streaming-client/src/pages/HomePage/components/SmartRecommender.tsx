import {Box, Stack, Typography} from "@mui/material";
import AutoAwesomeOutlinedIcon from "@mui/icons-material/AutoAwesomeOutlined";
import {HOME_PAGE_CONSTANTS} from "../../../constants/homePageConstants.ts";

type SmartRecommenderProps = {
  onClick?: () => void;
}

export function SmartRecommender({onClick}: SmartRecommenderProps) {
  return (
      <Box sx={{
        p: "32px",
        bgcolor: "background.paper",
        borderRadius: "12px",
        minHeight: "400px",
        width: "100%",
        cursor: "pointer",
      }} onClick={onClick}>
        <Stack sx={{
          justifyContent: "center",
          height: "100%",
        }}>
          <Stack spacing="8px" sx={{alignItems: "center"}}>
            <AutoAwesomeOutlinedIcon color="primary" fontSize="large"/>
            <Stack sx={{
              maxWidth: "150px"
            }}>
            <Typography variant="h6" sx={{
              fontWeight: "bold",
              textAlign: "center",
            }}>{HOME_PAGE_CONSTANTS.SMART_RECOMMENDER_TITLE}</Typography>
            <Typography variant="body2" sx={{
              color: "text.secondary",
              textAlign: "center",
            }}>
              {HOME_PAGE_CONSTANTS.SMART_RECOMMENDER_SUBTITLE}
            </Typography>
            </Stack>
          </Stack>
        </Stack>
      </Box>
  )
}