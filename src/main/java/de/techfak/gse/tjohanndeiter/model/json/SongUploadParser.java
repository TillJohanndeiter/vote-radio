package de.techfak.gse.tjohanndeiter.model.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.techfak.gse.tjohanndeiter.model.client.SongUpload;

public class SongUploadParser implements SongUploadJSonParser {


    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public SongUpload toSongUpload(final String json) throws JsonProcessingException {
        return objectMapper.readValue(json, SongUpload.class);
    }

    @Override
    public String toJson(final SongUpload song) throws JsonProcessingException {
        return objectMapper.writeValueAsString(song);
    }
}
