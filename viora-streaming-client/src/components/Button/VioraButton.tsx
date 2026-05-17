import {Button, CircularProgress, Typography, Stack} from "@mui/material";

type VioraButtonProps = {
  name: string;
  onClick?: () => void;
  type?: 'submit' | 'button' | 'reset';
  loading?: boolean;
  sx?: object;
};

export function VioraButton({name, onClick, type = 'submit', loading = false, sx}: VioraButtonProps) {
  return (
      <Button
          type={type}
          fullWidth
          disabled={loading}
          onClick={onClick}
          sx={{
            ...sx,
            mt: 3,
            py: 1.5,
            borderRadius: 3,
            border: "1px solid",
            borderColor: "primary.main",
            backgroundColor: loading ? "text.secondary" : "primary.main",
            color: "text.primary",
            transition: "all 0.2s ease",
            textTransform: "none",
            fontWeight: "bold",

            "&:hover:not(:disabled)": {
              backgroundColor: "transparent",
              borderColor: "primary.main",
            },

            "&:active:not(:disabled)" : {
              backgroundColor: "primary.main",
              color: "background.default",
            },

            "&:disabled": {
              backgroundColor: "text.disabled",
              color: "text.primary",
              borderColor: "text.disabled",
            },
          }}>
        {loading ? (
            <Stack spacing="15px" direction="row">
              <CircularProgress size={20}  color='secondary' sx={{display: 'block'}}/>
              <Typography variant="body2" >
                {name}
              </Typography>
            </Stack>
        ) : (
            name
        )}
      </Button>
  )
}
