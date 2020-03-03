package de.techfak.gse.tjohanndeiter.json;

import de.techfak.gse.tjohanndeiter.mode.client.SongUpload;

public interface SongUploadJSonParser {

    String toJson(SongUpload song) throws JsonException;

    SongUpload toSongUpload(String json) throws JsonException;
}
