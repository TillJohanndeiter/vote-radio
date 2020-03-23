package de.tjohanndeiter.model.player;


/**
 * Bean to transfer information about the length of current song, played time and remaining time form server
 * to client.
 */
public class TimeBean {

    private long length;
    private long playedTime;
    private long remainTime;

    private TimeBean() {
    }


    /**
     * Initialize {@link #length} and {@link #playedTime} and calculate {@link #remainTime}.
     * @param length length of song in milliseconds
     * @param playedTime time song has played
     */
    public TimeBean(final long length, final long playedTime) {
        this.length = length;
        this.playedTime = playedTime;
        remainTime = length - playedTime;
    }

    public long getLength() {
        return length;
    }

    public long getPlayedTime() {
        return playedTime;
    }

    public long getRemainTime() {
        return remainTime;
    }
}
