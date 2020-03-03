package de.techfak.gse.tjohanndeiter.json;

import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;

public interface QueueSongJsonParser {
    String toJson(QueueSong song) throws JsonException;

    QueueSong toSong(String json) throws JsonException;
}
