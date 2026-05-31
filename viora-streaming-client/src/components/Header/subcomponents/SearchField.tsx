import {Box, InputBase} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import {NAVIGATION_CONSTANTS} from "../../../constants/navigationConstants.ts";

export type SearchFieldProps = {
  onSearch: (query: string) => void;
};

export function SearchField({onSearch}: SearchFieldProps) {
  return (
      <Box
          sx={{
            display: "flex",
            alignItems: "center",
            backgroundColor: "secondary.main",
            borderRadius: "12px",
            p: "8px 16px",
            width: "100%",
            minWidth: "300px",
            maxWidth: "700px",
          }}
      >
        <SearchIcon sx={{color: "text.secondary", mr: "12px"}}/>
        <InputBase
            placeholder={NAVIGATION_CONSTANTS.SEARCH_PLACEHOLDER}
            fullWidth
            sx={{
              color: "text.primary",
              fontSize: "1.0rem",
              "&::placeholder": {
                color: "text.secondary",
                opacity: 1,
              },
            }}
            onChange={(e) => onSearch(e.target.value)}
        />
      </Box>
  );
}