import type {Mood} from "../types/moodTypes.ts";

export const ASSISTANT_DESCRIPTION = "Our AI analyzes your emotional frequency to curate a cinematic\n" +
    "journey that resonates with your current state of mind.";

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