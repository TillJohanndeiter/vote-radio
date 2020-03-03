package de.techfak.gse.tjohanndeiter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;

public class QueueSongJsonParserImpl implements QueueSongJsonParser {


    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public QueueSong toSong(final String json) throws JsonException {
        try {
            return objectMapper.readValue(json, QueueSong.class);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to deserialize Song", e);
        }
    }

    @Override
    public String toJson(final QueueSong song) throws JsonException {
        try {
            return objectMapper.writeValueAsString(song);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to serialize Song", e);
        }
    }
}
