package de.tjohanndeiter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.tjohanndeiter.mode.server.CurrentSong;

public class CurrentSongJsonParserImpl implements CurrentSongJsonParser {


    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public CurrentSong toCurrentSongResponse(final String json) throws JsonException {
        try {
            return objectMapper.readValue(json, CurrentSong.class);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to deserialize CurrentSong", e);
        }
    }

    @Override
    public String toJson(final CurrentSong currentSongResponse) throws JsonException {
        try {
            return objectMapper.writeValueAsString(currentSongResponse);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to serialize CurrentSong", e);
        }
    }
}
