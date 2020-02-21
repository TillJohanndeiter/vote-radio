package de.techfak.gse.tjohanndeiter.model.player;

public class TimeBean {

    private long length;
    private long played;
    private long remainTime;

    private TimeBean() {
    }

    public TimeBean(final long length, final long played) {
        this.length = length;
        this.played = played;
        remainTime = length - played;
    }

    public long getLength() {
        return length;
    }

    public long getPlayed() {
        return played;
    }

    public long getRemainTime() {
        return remainTime;
    }
}
