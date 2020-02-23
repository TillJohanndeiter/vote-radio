package de.techfak.gse.tjohanndeiter.model.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.techfak.gse.tjohanndeiter.model.server.User;

public interface UserJsonParser {
    String toJson(User song) throws JsonProcessingException;

    User toUser(String json) throws JsonProcessingException;
}
