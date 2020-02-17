package de.techfak.gse.tjohanndeiter.model.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;

public interface QueueSongJsonParser {
    String toJson(QueueSong song) throws JsonProcessingException;

    QueueSong toSong(String json) throws JsonProcessingException;
}
