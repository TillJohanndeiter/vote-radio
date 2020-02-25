package de.techfak.gse.tjohanndeiter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.techfak.gse.tjohanndeiter.model.player.TimeBean;

public interface TimeBeanJsonParser {

    TimeBean toTimeBean(String json) throws JsonProcessingException;

    String toJson(TimeBean timeBean) throws JsonProcessingException;
}
