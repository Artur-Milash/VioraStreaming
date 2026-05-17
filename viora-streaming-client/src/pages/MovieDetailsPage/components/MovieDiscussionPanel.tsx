import {useEffect, useRef, useState} from "react";
import {
  Box,
  CircularProgress,
  Divider,
  Drawer,
  IconButton,
  InputAdornment,
  OutlinedInput,
  Stack,
  Typography,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import SendIcon from "@mui/icons-material/Send";
import SmartToyOutlinedIcon from "@mui/icons-material/SmartToyOutlined";
import {useMovieDiscussion} from "../../../hooks/useMovieDiscussion.ts";

type Props = {
  movieId: number;
  movieTitle: string;
  open: boolean;
  onClose: () => void;
};

export function MovieDiscussionPanel({movieId, movieTitle, open, onClose}: Props) {
  const [input, setInput] = useState("");
  const bottomRef = useRef<HTMLDivElement>(null);
  const {messages, isLoadingMessages, isSending, send} = useMovieDiscussion(open ? movieId : null);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({behavior: "smooth"});
  }, [messages]);

  const handleSend = () => {
    const trimmed = input.trim();
    if (!trimmed || isSending) return;
    setInput("");
    send(trimmed);
  };

  return (
    <Drawer
      anchor="right"
      open={open}
      onClose={onClose}
      PaperProps={{sx: {width: 400, display: "flex", flexDirection: "column"}}}
    >
      <Stack
        direction="row"
        sx={{alignItems: "center", justifyContent: "space-between", p: "16px 20px"}}
      >
        <Stack direction="row" spacing="10px" sx={{alignItems: "center"}}>
          <Box
            sx={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              borderRadius: "100%",
              backgroundColor: "primary.main",
              height: "28px",
              width: "28px",
            }}
          >
            <SmartToyOutlinedIcon fontSize="small"/>
          </Box>
          <Stack>
            <Typography variant="body1" sx={{fontWeight: "bold"}}>
              Movie Assistant
            </Typography>
            <Typography variant="caption" sx={{color: "text.secondary"}}>
              {movieTitle}
            </Typography>
          </Stack>
        </Stack>
        <IconButton onClick={onClose} size="small">
          <CloseIcon/>
        </IconButton>
      </Stack>

      <Divider/>

      <Box sx={{flex: 1, overflowY: "auto", p: "16px", display: "flex", flexDirection: "column", gap: "12px"}}>
        {isLoadingMessages ? (
          <Box sx={{display: "flex", justifyContent: "center", pt: "32px"}}>
            <CircularProgress size={24}/>
          </Box>
        ) : messages.length === 0 ? (
          <Typography variant="body2" sx={{color: "text.secondary", textAlign: "center", pt: "32px"}}>
            Ask anything about this movie
          </Typography>
        ) : (
          messages.map((msg) => (
            <Box
              key={msg.id}
              sx={{
                alignSelf: msg.role === "USER" ? "flex-end" : "flex-start",
                maxWidth: "80%",
                backgroundColor: msg.role === "USER" ? "primary.main" : "background.paper",
                color: msg.role === "USER" ? "primary.contrastText" : "text.primary",
                borderRadius: msg.role === "USER" ? "16px 16px 4px 16px" : "16px 16px 16px 4px",
                px: "14px",
                py: "10px",
              }}
            >
              <Typography variant="body2" sx={{whiteSpace: "pre-wrap"}}>
                {msg.content}
              </Typography>
            </Box>
          ))
        )}
        {isSending && (
          <Box sx={{alignSelf: "flex-start", display: "flex", alignItems: "center", gap: "8px"}}>
            <CircularProgress size={16}/>
            <Typography variant="caption" sx={{color: "text.secondary"}}>
              Thinking…
            </Typography>
          </Box>
        )}
        <div ref={bottomRef}/>
      </Box>

      <Divider/>

      <Box sx={{p: "12px 16px"}}>
        <OutlinedInput
          fullWidth
          size="small"
          placeholder="Ask about this movie…"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && !e.shiftKey && handleSend()}
          disabled={isSending}
          endAdornment={
            <InputAdornment position="end">
              <IconButton onClick={handleSend} disabled={!input.trim() || isSending} size="small">
                <SendIcon fontSize="small"/>
              </IconButton>
            </InputAdornment>
          }
          sx={{borderRadius: "12px"}}
        />
      </Box>
    </Drawer>
  );
}
