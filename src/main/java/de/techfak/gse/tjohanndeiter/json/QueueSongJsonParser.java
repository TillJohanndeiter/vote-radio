package de.techfak.gse.tjohanndeiter.json;

import de.techfak.gse.tjohanndeiter.model.playlist.VotedSong;

public interface QueueSongJsonParser {
    String toJson(VotedSong song) throws JsonException;

    VotedSong toSong(String json) throws JsonException;
}
