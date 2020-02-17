package de.techfak.gse.tjohanndeiter.model.playlist;

import de.techfak.gse.tjohanndeiter.model.database.Song;

import java.util.Objects;

public class QueueSong extends Song {

    private int voteCount;
    private int playsBeforeReplay;
    private int id;

    private QueueSong() {
        super();
    }

    /**
     * Set {@link #voteCount} to zero and set the id.
     *
     * @param song copy song
     * @param id   id of Song
     */
    public QueueSong(final Song song, final int id, final int playsBeforeReplay) {
        super(song);
        voteCount = 0;
        this.id = id;
        this.playsBeforeReplay = playsBeforeReplay;
    }

    public QueueSong(final QueueSong queueSong) {
        super(queueSong);
        voteCount = queueSong.voteCount;
        id = queueSong.id;
        playsBeforeReplay = queueSong.playsBeforeReplay;
    }

    public int getVoteCount() {
        return voteCount;
    }

    int getPlaysBeforeReplay() {
        return playsBeforeReplay;
    }

    public int getId() {
        return id;
    }

    void decReplayCount() {
        if (playsBeforeReplay > 0) {
            playsBeforeReplay--;
        }
    }

    void increaseVote() {
        voteCount++;
    }

    void resetVote() {
        voteCount = 0;
    }


    public void setPlaysBeforeReplay(final int playsBeforeReplay) {
        this.playsBeforeReplay = playsBeforeReplay;
    }

    @Override
    public String toString() {
        return "voteCount=" + voteCount
                + "ID=" + id + '}' + super.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true; //NOPMD
        }
        if (!(o instanceof QueueSong)) {
            return false; //NOPMD
        }
        if (!super.equals(o)) {
            return false; //NOPMD
        }
        final QueueSong queueSong = (QueueSong) o;
        return id == queueSong.id && voteCount == queueSong.voteCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
