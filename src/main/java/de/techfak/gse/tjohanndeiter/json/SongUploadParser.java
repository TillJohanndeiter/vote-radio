package de.techfak.gse.tjohanndeiter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.techfak.gse.tjohanndeiter.mode.client.SongUpload;

public class SongUploadParser implements SongUploadJSonParser {


    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public SongUpload toSongUpload(final String json) throws JsonProcessingException {
        return objectMapper.readValue(json, SongUpload.class);
    }

    @Override
    public String toJson(final SongUpload song) throws JsonProcessingException {
        return objectMapper.writeValueAsString(song);
    }
}
