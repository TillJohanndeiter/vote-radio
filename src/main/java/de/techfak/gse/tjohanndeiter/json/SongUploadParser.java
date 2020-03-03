package de.techfak.gse.tjohanndeiter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.techfak.gse.tjohanndeiter.mode.client.SongUpload;

public class SongUploadParser implements SongUploadJSonParser {


    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public SongUpload toSongUpload(final String json) throws JsonException {
        try {
            return objectMapper.readValue(json, SongUpload.class);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to deserialize SongUpload", e);
        }
    }

    @Override
    public String toJson(final SongUpload song) throws JsonException {
        try {
            return objectMapper.writeValueAsString(song);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to serialize SongUpload", e);
        }
    }
}
