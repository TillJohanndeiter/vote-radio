package de.techfak.gse.tjohanndeiter.model.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.techfak.gse.tjohanndeiter.model.server.User;

public class UserJsonParserImpl implements UserJsonParser {



    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public User toUser(final String json) throws JsonProcessingException {
        return objectMapper.readValue(json, User.class);
    }

    @Override
    public String toJson(final User user) throws JsonProcessingException {
        return objectMapper.writeValueAsString(user);
    }
}
