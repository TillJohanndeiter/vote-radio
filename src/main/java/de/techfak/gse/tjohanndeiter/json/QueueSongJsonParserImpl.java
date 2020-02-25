package de.techfak.gse.tjohanndeiter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;

public class QueueSongJsonParserImpl implements QueueSongJsonParser {



    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public QueueSong toSong(final String json) throws JsonProcessingException {
        return objectMapper.readValue(json, QueueSong.class);
    }

    @Override
    public String toJson(final QueueSong song) throws JsonProcessingException {
        return objectMapper.writeValueAsString(song);
    }
}
