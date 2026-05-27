import {Box, InputBase, IconButton} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import {useRef} from "react";

export type SearchFieldProps = {
  onSearch: (query: string) => void;
  onEnter: (query: string) => void;
};

export function SearchField({onSearch, onEnter}: SearchFieldProps) {
  const inputRef = useRef<HTMLInputElement>(null);

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
        <InputBase
            inputRef={inputRef}
            placeholder="Search movies, genres..."
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
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                onEnter((e.target as HTMLInputElement).value);
              }
            }}
        />
        <IconButton
            onClick={() => {
              if (inputRef.current) {
                onEnter(inputRef.current.value);
              }
            }}
            sx={{color: "text.secondary", p: "4px"}}
        >
          <SearchIcon/>
        </IconButton>
      </Box>
  );
}