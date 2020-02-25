package de.techfak.gse.tjohanndeiter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;

public interface VoteListJsonParser {
    String toJson(Playlist playlist) throws JsonProcessingException;

    VoteList toPlaylist(String json) throws JsonProcessingException;
}
