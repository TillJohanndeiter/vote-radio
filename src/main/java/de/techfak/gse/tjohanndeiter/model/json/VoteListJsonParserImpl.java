package de.techfak.gse.tjohanndeiter.model.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;

public class VoteListJsonParserImpl implements VoteListJsonParser {

    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public VoteList toPlaylist(final String json) throws JsonProcessingException {
        return objectMapper.readValue(json, VoteList.class);
    }

    @Override
    public String toJson(final Playlist playlist) throws JsonProcessingException {
        return objectMapper.writeValueAsString(playlist);
    }
}
