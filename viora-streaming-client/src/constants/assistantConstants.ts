import type {Mood} from "../types/moodTypes.ts";

export const ASSISTANT_DESCRIPTION = "Our AI analyzes your emotional frequency to curate a cinematic\n" +
    "journey that resonates with your current state of mind.";

export const ASSISTANT_UI = {
  // ChooseMoodSection
  MOOD_TITLE: "What's your mood?",
  FIND_MOVIES_BUTTON: "Find my movies",

  // RecommendationSection
  NEURAL_SELECTION_LABEL: "THE NEURAL SELECTION",
  AI_RECOMMENDATIONS_TITLE: "AI Recommendations",
  WATCH_NOW_BUTTON: "Watch Now",
  DETAILS_BUTTON: "Details",

  // MovieDiscussionPanel
  AI_PANEL_NAME: "Viora AI",
  AI_PANEL_SUBTITLE: "Movie Expert & Stylist",
  ASK_MOVIE_EMPTY_STATE: "Ask anything about this movie",
  ASK_MOVIE_INPUT_PLACEHOLDER: "Ask about this movie…",
  MESSAGE_SENDER_USER: "YOU",
  MESSAGE_SENDER_AI: "AI",
  AI_THINKING_TEXT: "Thinking…",

  // Player AI chip and tooltips
  AI_CHIP_LABEL: "AI",
  REPLAY_TOOLTIP: "Replay 10s",
  FORWARD_TOOLTIP: "Forward 10s",
  EXIT_FULLSCREEN_TOOLTIP: "Exit Fullscreen",
  FULLSCREEN_TOOLTIP: "Fullscreen",
} as const;

export const MOODS: Mood[] = [
  {
    emoji: "⚡ ",
    name: "Electrified",
    description: "High-octane action",
  },
  {
    emoji: "🌊",
    name: "Serene",
    description: "Peaceful narrated",
  },
  {
    emoji: "🕵️‍♂️️",
    name: "Cerebral",
    description: "Complex mysteries",
  },
  {
    emoji: "🕯️️",
    name: "Melancholic",
    description: "Poignant drama",
  },
  {
    emoji: "🚀️",
    name: "Adventurous",
    description: "Grand scale epic",
  },
  {
    emoji: "🤡️",
    name: "Whimsical",
    description: "Lighthearted fun",
  },
  {
    emoji: "🩸️",
    name: "Visceral",
    description: "Intense horror",
  },
  {
    emoji: "💖",
    name: "Intimate️",
    description: "Human connection",
  },
]