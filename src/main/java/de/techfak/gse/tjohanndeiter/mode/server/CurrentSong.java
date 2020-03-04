package de.techfak.gse.tjohanndeiter.mode.server;

import de.techfak.gse.tjohanndeiter.model.player.TimeBean;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;

public class CurrentSong {
    private QueueSong queueSong;
    private TimeBean timeBean;

    private CurrentSong() {

    }

    public CurrentSong(final QueueSong currentSong, final TimeBean timeBean) {
        this.queueSong = currentSong;
        this.timeBean = timeBean;
    }

    public QueueSong getQueueSong() {
        return queueSong;
    }

    public TimeBean getTimeBean() {
        return timeBean;
    }
}
