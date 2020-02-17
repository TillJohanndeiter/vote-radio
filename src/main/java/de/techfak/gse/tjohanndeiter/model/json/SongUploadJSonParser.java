package de.techfak.gse.tjohanndeiter.model.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.techfak.gse.tjohanndeiter.model.client.SongUpload;

public interface SongUploadJSonParser {

    String toJson(SongUpload song) throws JsonProcessingException;

    SongUpload toSongUpload(String json) throws JsonProcessingException;
}
