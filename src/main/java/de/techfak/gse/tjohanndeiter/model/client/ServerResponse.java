package de.techfak.gse.tjohanndeiter.model.client;

import de.techfak.gse.tjohanndeiter.model.player.TimeBean;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;

public class ServerResponse {

    private QueueSong queueSong;
    private TimeBean timeBean;

    public ServerResponse(final QueueSong queueSong, final TimeBean timeBean) {
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
