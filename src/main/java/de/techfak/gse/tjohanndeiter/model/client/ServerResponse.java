package de.techfak.gse.tjohanndeiter.model.client;

import de.techfak.gse.tjohanndeiter.model.player.TimeBean;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;

/**
 * Bean for current song request and played request. So when Client first time connect to the server.
 * Coupled both together for less complexity in MVC Pattern.
 */
public class ServerResponse {

    private final QueueSong queueSong;
    private final TimeBean timeBean;

    ServerResponse(final QueueSong queueSong, final TimeBean timeBean) {
        this.queueSong = queueSong;
        this.timeBean = timeBean;
    }

    public QueueSong getQueueSong() {
        return queueSong;
    }

    public TimeBean getTimeBean() {
        return timeBean;
    }
}
