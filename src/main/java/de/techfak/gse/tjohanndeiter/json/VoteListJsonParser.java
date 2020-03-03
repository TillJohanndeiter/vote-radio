package de.techfak.gse.tjohanndeiter.json;

import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;

public interface VoteListJsonParser {
    String toJson(Playlist playlist) throws JsonException;

    VoteList toPlaylist(String json) throws JsonException;
}
