package de.techfak.gse.tjohanndeiter.json;

import de.techfak.gse.tjohanndeiter.mode.server.StreamUrl;

public interface StreamUrlJsonParser {
    String toJson(StreamUrl streamUrl) throws JsonException;

    StreamUrl toCurrentSongResponse(String json) throws JsonException;
}
