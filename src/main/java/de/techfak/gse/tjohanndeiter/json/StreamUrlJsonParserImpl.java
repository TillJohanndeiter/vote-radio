package de.techfak.gse.tjohanndeiter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.techfak.gse.tjohanndeiter.mode.server.StreamUrl;

public class StreamUrlJsonParserImpl implements StreamUrlJsonParser {


    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public StreamUrl toCurrentSongResponse(final String json) throws JsonException {
        try {
            return objectMapper.readValue(json, StreamUrl.class);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to deserialize StreamUrl", e);
        }
    }

    @Override
    public String toJson(final StreamUrl streamUrl) throws JsonException {
        try {
            return objectMapper.writeValueAsString(streamUrl);
        } catch (final JsonProcessingException e) {
            throw new JsonException("ObjectMapper failed to serialize StreamUrl", e);
        }
    }
}
