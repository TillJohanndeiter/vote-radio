package de.tjohanndeiter.mode.server;

import de.tjohanndeiter.model.player.TimeBean;
import de.tjohanndeiter.model.playlist.VotedSong;

public class CurrentSong {
    private VotedSong queueSong;
    private TimeBean timeBean;

    private CurrentSong() {

    }

    public CurrentSong(final VotedSong currentSong, final TimeBean timeBean) {
        this.queueSong = currentSong;
        this.timeBean = timeBean;
    }

    public VotedSong getQueueSong() {
        return queueSong;
    }

    public TimeBean getTimeBean() {
        return timeBean;
    }
}
