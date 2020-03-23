package de.tjohanndeiter.json;

import de.tjohanndeiter.mode.client.SongUpload;

public interface SongUploadJSonParser {

    String toJson(SongUpload song) throws JsonException;

    SongUpload toSongUpload(String json) throws JsonException;
}
