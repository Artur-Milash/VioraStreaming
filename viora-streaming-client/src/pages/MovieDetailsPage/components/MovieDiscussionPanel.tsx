import {useEffect, useRef, useState} from "react";
import type {SxProps, Theme} from "@mui/material";
import {
  Box,
  CircularProgress,
  Divider,
  IconButton,
  InputAdornment,
  OutlinedInput,
  Stack,
  Typography,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import SendIcon from "@mui/icons-material/Send";
import SmartToyIcon from '@mui/icons-material/SmartToy';
import {useMovieDiscussion} from "../../../hooks/useMovieDiscussion.ts";
import {ASSISTANT_UI} from "../../../constants/assistantConstants.ts";

type Props = {
  movieId: number;
  onClose: () => void;
  sx?: SxProps<Theme>;
};

export function MovieDiscussionPanel({movieId, onClose, sx}: Props) {
  const [input, setInput] = useState("");
  const bottomRef = useRef<HTMLDivElement>(null);
  const {messages, isLoadingMessages, isSending, send} = useMovieDiscussion(movieId);

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
      <Box sx={{
        width: "512px",
        flexShrink: 0,
        display: "flex",
        flexDirection: "column",
        backgroundColor: "background.paper",
        borderLeft: "1px solid",
        borderColor: "divider",
        ...sx,
      }}>
        <Stack
            direction="row"
            sx={{alignItems: "center", justifyContent: "space-between", p: "16px 20px", flexShrink: 0}}
        >
          <Stack direction="row" spacing="10px" sx={{alignItems: "center"}}>
            <Box sx={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              borderRadius: "12px",
              backgroundColor: "primary.main",
              height: "40px",
              width: "40px",
            }}>
              <SmartToyIcon fontSize="small"/>
            </Box>
            <Stack>
              <Typography variant="body1" sx={{fontWeight: "bold"}}>
                {ASSISTANT_UI.AI_PANEL_NAME}
              </Typography>
              <Typography variant="caption" sx={{color: "text.secondary", lineHeight: 1.2}}>
                {ASSISTANT_UI.AI_PANEL_SUBTITLE}
              </Typography>
            </Stack>
          </Stack>
          <IconButton onClick={onClose} size="small">
            <CloseIcon/>
          </IconButton>
        </Stack>

        <Divider/>

        <Box sx={{
          flex: 1,
          overflowY: "auto",
          p: "16px",
          display: "flex",
          flexDirection: "column",
          gap: "12px",
          "&::-webkit-scrollbar": {width: "6px"},
          "&::-webkit-scrollbar-track": {background: "#1A1A1F", borderRadius: "3px"},
          "&::-webkit-scrollbar-thumb": {background: "#3B1F7A", borderRadius: "3px"},
          "&::-webkit-scrollbar-thumb:hover": {background: "#7C3AED"},
        }}>
          {isLoadingMessages ? (
              <Box sx={{display: "flex", justifyContent: "center", pt: "32px"}}>
                <CircularProgress size={24}/>
              </Box>
          ) : messages.length === 0 ? (
              <Typography variant="body2" sx={{color: "text.secondary", textAlign: "center", pt: "32px"}}>
                {ASSISTANT_UI.ASK_MOVIE_EMPTY_STATE}
              </Typography>
          ) : (
              messages.map((msg) => {
                const isUser = msg.role === "USER";
                const time = new Date(msg.createdAt).toLocaleTimeString([], {hour: "2-digit", minute: "2-digit"});
                return (
                    <Box
                        key={msg.id}
                        sx={{alignSelf: isUser ? "flex-end" : "flex-start", maxWidth: "80%", display: "flex", flexDirection: "column", gap: "4px"}}
                    >
                      <Box sx={{
                        backgroundColor: isUser ? "primary.main" : "background.default",
                        color: isUser ? "primary.contrastText" : "text.primary",
                        borderRadius: isUser ? "16px 16px 4px 16px" : "16px 16px 16px 4px",
                        px: "14px",
                        py: "10px",
                      }}>
                        <Typography variant="body2" sx={{whiteSpace: "pre-wrap"}}>
                          {msg.content}
                        </Typography>
                      </Box>
                      <Typography variant="caption" sx={{color: "text.disabled", alignSelf: isUser ? "flex-end" : "flex-start", px: "4px"}}>
                        {isUser ? ASSISTANT_UI.MESSAGE_SENDER_USER : ASSISTANT_UI.MESSAGE_SENDER_AI} · {time}
                      </Typography>
                    </Box>
                );
              })
          )}
          {isSending && (
              <Box sx={{alignSelf: "flex-start", display: "flex", alignItems: "center", gap: "8px"}}>
                <CircularProgress size={16}/>
                <Typography variant="caption" sx={{color: "text.secondary"}}>{ASSISTANT_UI.AI_THINKING_TEXT}</Typography>
              </Box>
          )}
          <div ref={bottomRef}/>
        </Box>

        <Divider/>

        <Box sx={{p: "12px 16px", flexShrink: 0}}>
          <OutlinedInput
              fullWidth
              size="small"
              placeholder={ASSISTANT_UI.ASK_MOVIE_INPUT_PLACEHOLDER}
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
      </Box>
  );
}
