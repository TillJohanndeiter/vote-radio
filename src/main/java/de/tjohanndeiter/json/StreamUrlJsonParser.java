package de.tjohanndeiter.json;

import de.tjohanndeiter.mode.server.StreamUrl;

public interface StreamUrlJsonParser {
    String toJson(StreamUrl streamUrl) throws JsonException;

    StreamUrl toCurrentSongResponse(String json) throws JsonException;
}
