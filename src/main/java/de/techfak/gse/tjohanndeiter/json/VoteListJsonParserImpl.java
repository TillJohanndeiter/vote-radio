package de.techfak.gse.tjohanndeiter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;

public class VoteListJsonParserImpl implements VoteListJsonParser {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public VoteList toPlaylist(final String json) throws JsonException {
        try {
            return objectMapper.readValue(json, VoteList.class);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to deserialize VoteList", e);
        }
    }

    @Override
    public String toJson(final Playlist playlist) throws JsonException {
        try {
            return objectMapper.writeValueAsString(playlist);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to serialize VoteList", e);
        }
    }
}
