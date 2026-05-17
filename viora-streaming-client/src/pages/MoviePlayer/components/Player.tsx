import {useEffect, useRef, useState, useCallback} from "react";
import {
  Box,
  IconButton,
  Slider,
  Typography,
  Tooltip,
  Fade,
  CircularProgress,
  Stack,
  Chip,
} from "@mui/material";
import {
  PlayArrowOutlined,
  PlayArrow,
  Pause,
  VolumeUp,
  VolumeOff,
  VolumeMute,
  Fullscreen,
  FullscreenExit,
  Replay10,
  Forward10,
  AutoAwesome,
} from "@mui/icons-material";
import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import {useHls} from "../../../hooks/useHls.ts";
import {API_PATHS} from "../../../constants/apiConstants.ts";
import {MovieDiscussionPanel} from "../../MovieDetailsPage/components/MovieDiscussionPanel.tsx";

interface PlayerProps {
  movieId: string;
  dbMovieId: number;
  apiBaseUrl: string;
  title?: string;
  onClose?: () => void;
  startFrom?: number;
}

function formatTime(seconds: number): string {
  if (isNaN(seconds) || seconds < 0) return "0:00";
  const h = Math.floor(seconds / 3600);
  const m = Math.floor((seconds % 3600) / 60);
  const s = Math.floor(seconds % 60);
  if (h > 0)
    return `${h}:${String(m).padStart(2, "0")}:${String(s).padStart(2, "0")}`;
  return `${m}:${String(s).padStart(2, "0")}`;
}

function formatRemaining(current: number, total: number): string {
  const remaining = total - current;
  return isNaN(remaining) || remaining <= 0 ? "-0:00" : `-${formatTime(remaining)}`;
}

export function Player({
                         movieId,
                         dbMovieId,
                         apiBaseUrl,
                         title = "Now Playing",
                         onClose,
                         startFrom
                       }: PlayerProps) {
  const videoRef = useRef<HTMLVideoElement>(null);
  const containerRef = useRef<HTMLDivElement>(null);
  const controlsTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  const [isPlaying, setIsPlaying] = useState(false);
  const [currentTime, setCurrentTime] = useState(0);
  const [duration, setDuration] = useState(0);
  const [volume, setVolume] = useState(1);
  const [isMuted, setIsMuted] = useState(false);
  const [isFullscreen, setIsFullscreen] = useState(false);
  const [isVideoLoading, setIsVideoLoading] = useState(true);
  const [showControls, setShowControls] = useState(true);
  const [chatOpen, setChatOpen] = useState(false);

  const playlistUrl = `${apiBaseUrl}${API_PATHS.streaming}/${movieId}/index.m3u8`;

  const {isHlsLoading, hlsError} = useHls({
    src: playlistUrl,
    videoRef,
    startFrom
  });

  const isLoading = isHlsLoading || isVideoLoading;

  useEffect(() => {
    const video = videoRef.current;
    if (!video) return;
    const onTimeUpdate = () => setCurrentTime(video.currentTime);
    const onDurationChange = () => setDuration(video.duration);
    const onPlay = () => setIsPlaying(true);
    const onPause = () => setIsPlaying(false);
    const onWaiting = () => setIsVideoLoading(true);
    const onPlaying = () => setIsVideoLoading(false);
    const onCanPlay = () => setIsVideoLoading(false);
    const onVolumeChange = () => {
      setVolume(video.volume);
      setIsMuted(video.muted);
    };
    video.addEventListener("timeupdate", onTimeUpdate);
    video.addEventListener("durationchange", onDurationChange);
    video.addEventListener("play", onPlay);
    video.addEventListener("pause", onPause);
    video.addEventListener("waiting", onWaiting);
    video.addEventListener("playing", onPlaying);
    video.addEventListener("canplay", onCanPlay);
    video.addEventListener("volumechange", onVolumeChange);
    return () => {
      video.removeEventListener("timeupdate", onTimeUpdate);
      video.removeEventListener("durationchange", onDurationChange);
      video.removeEventListener("play", onPlay);
      video.removeEventListener("pause", onPause);
      video.removeEventListener("waiting", onWaiting);
      video.removeEventListener("playing", onPlaying);
      video.removeEventListener("canplay", onCanPlay);
      video.removeEventListener("volumechange", onVolumeChange);
    };
  }, []);

  useEffect(() => {
    const onChange = () => setIsFullscreen(!!document.fullscreenElement);
    document.addEventListener("fullscreenchange", onChange);
    return () => document.removeEventListener("fullscreenchange", onChange);
  }, []);

  const resetControlsTimer = useCallback(() => {
    setShowControls(true);
    if (controlsTimerRef.current) clearTimeout(controlsTimerRef.current);
    controlsTimerRef.current = setTimeout(() => {
      if (isPlaying) setShowControls(false);
    }, 3000);
  }, [isPlaying]);

  const togglePlay = () => {
    const v = videoRef.current;
    if (!v) return;
    v.paused ? v.play() : v.pause();
  };

  const seek = (_: Event, value: number | number[]) => {
    const v = videoRef.current;
    if (!v) return;
    v.currentTime = value as number;
    setCurrentTime(value as number);
  };

  const skip = (seconds: number) => {
    const v = videoRef.current;
    if (!v) return;
    v.currentTime = Math.min(Math.max(v.currentTime + seconds, 0), duration);
  };

  const handleVolumeChange = (_: Event, value: number | number[]) => {
    const v = videoRef.current;
    if (!v) return;
    const val = value as number;
    v.volume = val;
    v.muted = val === 0;
  };

  const toggleMute = () => {
    const v = videoRef.current;
    if (!v) return;
    v.muted = !v.muted;
  };

  const toggleFullscreen = () => {
    const el = containerRef.current;
    if (!el) return;
    if (!document.fullscreenElement) el.requestFullscreen();
    else document.exitFullscreen();
  };

  const VolumeIcon =
      isMuted || volume === 0 ? VolumeOff : volume < 0.5 ? VolumeMute : VolumeUp;

  return (
      <Box
          ref={containerRef}
          sx={{
            position: "fixed",
            inset: 0,
            zIndex: 1400,
            display: "flex",
          }}
      >
        {/* ── Video area ── */}
        <Box
            onMouseMove={resetControlsTimer}
            onMouseLeave={() => isPlaying && setShowControls(false)}
            sx={{
              flex: 1,
              position: "relative",
              bgcolor: "#000",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              cursor: showControls ? "default" : "none",
              minWidth: 0,
            }}
        >
          <Box
              component="video"
              ref={videoRef}
              onClick={togglePlay}
              sx={{width: "100%", height: "100%", objectFit: "contain", display: "block"}}
          />

          {isLoading && (
              <Box sx={{
                position: "absolute",
                inset: 0,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                pointerEvents: "none",
              }}>
                <CircularProgress color="primary" size={64} thickness={2}/>
              </Box>
          )}

          {hlsError && (
              <Box sx={{
                position: "absolute",
                inset: 0,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                pointerEvents: "none",
              }}>
                <Typography color="error" variant="body2">{hlsError}</Typography>
              </Box>
          )}

          <Fade in={showControls}>
            <Box
                sx={{
                  position: "absolute",
                  inset: 0,
                  display: "flex",
                  flexDirection: "column",
                  justifyContent: "space-between",
                  background:
                      "linear-gradient(to bottom, rgba(0,0,0,0.72) 0%, transparent 18%, transparent 68%, rgba(0,0,0,0.88) 100%)",
                }}
            >
              <Stack direction="row" spacing="16px" sx={{p: "24px 32px", alignItems: "center"}}>
                {onClose && (
                    <IconButton
                        onClick={onClose}
                        disableRipple
                        sx={{p: 0, "&:hover": {bgcolor: "transparent"}}}
                    >
                      <Box sx={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        height: 44,
                        width: 44,
                        bgcolor: "secondary.main",
                        borderRadius: "50%",
                        transition: "opacity 0.2s",
                        "&:hover": {opacity: 0.75},
                      }}>
                        <KeyboardBackspaceIcon sx={{color: "text.primary", fontSize: 20}}/>
                      </Box>
                    </IconButton>
                )}

                <Box sx={{flex: 1}}>
                  <Typography variant="h6" sx={{color: "text.primary", fontWeight: 700, lineHeight: 1.2}}>
                    {title}
                  </Typography>
                </Box>

                <Chip
                    icon={<AutoAwesome sx={{fontSize: 15}}/>}
                    label="AI"
                    size="small"
                    onClick={() => setChatOpen((o) => !o)}
                    sx={{
                      border: "1px solid",
                      borderColor: "primary.main",
                      color: chatOpen ? "primary.contrastText" : "primary.main",
                      backgroundColor: chatOpen ? "primary.main" : "transparent",
                      fontWeight: 700,
                      letterSpacing: 0.5,
                      height: 28,
                      cursor: "pointer",
                      "& .MuiChip-icon": {ml: "6px", color: chatOpen ? "primary.contrastText" : "primary.main"},
                      "&:hover": {backgroundColor: chatOpen ? "primary.dark" : "rgba(255,255,255,0.08)"},
                    }}
                />
              </Stack>

              <Box sx={{display: "flex", alignItems: "center", justifyContent: "center", gap: 3}}>
                <Tooltip title="Replay 10s">
                  <IconButton onClick={() => skip(-10)} sx={centerGhostBtnSx}>
                    <Replay10 sx={{fontSize: 36, color: "text.secondary"}}/>
                  </IconButton>
                </Tooltip>

                <IconButton
                    onClick={togglePlay}
                    sx={{
                      color: "#fff",
                      bgcolor: "primary.main",
                      width: 64,
                      height: 64,
                      transition: "all 0.18s",
                      "&:hover": {bgcolor: "primary.dark", transform: "scale(1.08)"},
                    }}
                >
                  {isPlaying ? <Pause sx={{fontSize: 38}}/> : <PlayArrow sx={{fontSize: 38}}/>}
                </IconButton>

                <Tooltip title="Forward 10s">
                  <IconButton onClick={() => skip(10)} sx={centerGhostBtnSx}>
                    <Forward10 sx={{fontSize: 36, color: "text.secondary"}}/>
                  </IconButton>
                </Tooltip>
              </Box>

              <Box sx={{px: "20px", pb: "20px"}}>
                <Stack spacing="14px">
                  <Box sx={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "space-between",
                    color: "text.primary",
                  }}>
                    <Typography sx={timeSx}>{formatTime(currentTime)}</Typography>
                    <Typography sx={timeSx}>{formatRemaining(currentTime, duration)}</Typography>
                  </Box>
                  <Box sx={{flex: 1}}>
                    <Slider
                        min={0}
                        max={duration || 1}
                        value={currentTime}
                        onChange={seek}
                        sx={progressSliderSx}
                    />
                  </Box>
                </Stack>

                <Stack direction="row" sx={{alignItems: "center", justifyContent: "space-between"}}>
                  <Stack direction="row" spacing="2px" sx={{alignItems: "center"}}>
                    <IconButton onClick={togglePlay} sx={bottomBtnSx}>
                      {isPlaying ? <Pause/> : <PlayArrowOutlined/>}
                    </IconButton>
                    <IconButton onClick={toggleMute} sx={{...bottomBtnSx, ml: "8px !important"}}>
                      <VolumeIcon/>
                    </IconButton>
                    <Slider
                        min={0}
                        max={1}
                        step={0.01}
                        value={isMuted ? 0 : volume}
                        onChange={handleVolumeChange}
                        sx={{...volumeSliderSx, width: 200}}
                    />
                  </Stack>

                  <Box sx={{flex: 1}}/>

                  <Tooltip title={isFullscreen ? "Exit Fullscreen" : "Fullscreen"}>
                    <IconButton onClick={toggleFullscreen} sx={bottomBtnSx}>
                      {isFullscreen ? <FullscreenExit/> : <Fullscreen/>}
                    </IconButton>
                  </Tooltip>
                </Stack>
              </Box>
            </Box>
          </Fade>
        </Box>

        {/* ── Chat panel ── */}
        {chatOpen && (
            <MovieDiscussionPanel
                movieId={dbMovieId}
                onClose={() => setChatOpen(false)}
                sx={{height: "100%", borderLeft: "1px solid rgba(255,255,255,0.1)"}}
            />
        )}
      </Box>
  );
}

// ─── SX ──────────────────────────────────────────────────────────────────────

const centerGhostBtnSx = {
  color: "#fff",
  transition: "all 0.18s",
  "&:hover": {transform: "scale(1.08)", bgcolor: "rgba(255,255,255,0.08)"},
};

const bottomBtnSx = {
  color: "rgba(255,255,255,0.85)",
  "&:hover": {color: "#fff", bgcolor: "rgba(255,255,255,0.08)"},
  "&.Mui-disabled": {opacity: 0.3},
  transition: "all 0.15s",
};

const timeSx = {
  color: "text.primary",
  fontFamily: "monospace",
  fontSize: "0.78rem",
  userSelect: "none" as const,
  minWidth: 40,
  textAlign: "center" as const,
};

const progressSliderSx = {
  color: "primary.main",
  height: 4,
  padding: "10px 0",
  "& .MuiSlider-thumb": {
    width: 14,
    height: 14,
    transition: "width 0.1s, height 0.1s",
    "&:hover, &.Mui-focusVisible": {width: 18, height: 18},
  },
  "& .MuiSlider-rail": {bgcolor: "rgba(255,255,255,0.2)", opacity: 1},
  "& .MuiSlider-track": {border: "none"},
};

const volumeSliderSx = {
  color: "primary.main",
  height: 3,
  "& .MuiSlider-thumb": {width: 12, height: 12},
  "& .MuiSlider-rail": {bgcolor: "rgba(255,255,255,0.25)", opacity: 1},
};