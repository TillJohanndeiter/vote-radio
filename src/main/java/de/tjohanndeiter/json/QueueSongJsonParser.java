package de.tjohanndeiter.json;

import de.tjohanndeiter.model.playlist.VotedSong;

public interface QueueSongJsonParser {
    String toJson(VotedSong song) throws JsonException;

    VotedSong toSong(String json) throws JsonException;
}
