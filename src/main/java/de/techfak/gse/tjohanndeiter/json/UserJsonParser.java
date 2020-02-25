package de.techfak.gse.tjohanndeiter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.techfak.gse.tjohanndeiter.mode.server.User;

public interface UserJsonParser {
    String toJson(User song) throws JsonProcessingException;

    User toUser(String json) throws JsonProcessingException;
}
