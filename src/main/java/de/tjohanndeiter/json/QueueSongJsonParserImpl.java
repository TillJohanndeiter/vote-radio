package de.tjohanndeiter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tjohanndeiter.model.playlist.VotedSong;

public class QueueSongJsonParserImpl implements QueueSongJsonParser {


    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public VotedSong toSong(final String json) throws JsonException {
        try {
            return objectMapper.readValue(json, VotedSong.class);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to deserialize Song", e);
        }
    }

    @Override
    public String toJson(final VotedSong song) throws JsonException {
        try {
            return objectMapper.writeValueAsString(song);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to serialize Song", e);
        }
    }
}
