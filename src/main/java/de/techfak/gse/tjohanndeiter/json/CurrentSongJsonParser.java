package de.techfak.gse.tjohanndeiter.json;

import de.techfak.gse.tjohanndeiter.mode.server.CurrentSong;

public interface CurrentSongJsonParser {
    String toJson(CurrentSong currentSongResponse) throws JsonException;

    CurrentSong toCurrentSongResponse(String json) throws JsonException;
}
