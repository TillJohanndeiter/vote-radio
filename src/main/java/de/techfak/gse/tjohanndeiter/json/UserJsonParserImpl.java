package de.techfak.gse.tjohanndeiter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.techfak.gse.tjohanndeiter.mode.server.User;

public class UserJsonParserImpl implements UserJsonParser {



    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public User toUser(final String json) throws JsonProcessingException {
        return objectMapper.readValue(json, User.class);
    }

    @Override
    public String toJson(final User user) throws JsonProcessingException {
        return objectMapper.writeValueAsString(user);
    }
}
