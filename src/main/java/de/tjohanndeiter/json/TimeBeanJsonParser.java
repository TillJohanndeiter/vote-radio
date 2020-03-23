package de.tjohanndeiter.json;

        import de.tjohanndeiter.model.player.TimeBean;

public interface TimeBeanJsonParser {

    TimeBean toTimeBean(String json) throws JsonException;

    String toJson(TimeBean timeBean) throws JsonException;
}
