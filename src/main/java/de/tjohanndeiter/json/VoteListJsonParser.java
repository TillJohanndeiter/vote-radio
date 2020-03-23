package de.tjohanndeiter.json;

import de.tjohanndeiter.model.playlist.Playlist;
import de.tjohanndeiter.model.playlist.VoteList;

public interface VoteListJsonParser {
    String toJson(Playlist playlist) throws JsonException;

    VoteList toPlaylist(String json) throws JsonException;
}
