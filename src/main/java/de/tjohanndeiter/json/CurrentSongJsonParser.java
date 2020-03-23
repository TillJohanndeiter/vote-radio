package de.tjohanndeiter.json;

import de.tjohanndeiter.mode.server.CurrentSong;

public interface CurrentSongJsonParser {
    String toJson(CurrentSong currentSongResponse) throws JsonException;

    CurrentSong toCurrentSongResponse(String json) throws JsonException;
}
