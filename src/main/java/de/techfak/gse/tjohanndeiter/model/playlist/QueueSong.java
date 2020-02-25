package de.techfak.gse.tjohanndeiter.model.playlist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.mode.server.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Extends {@link Song} form database with an unique id, voteCount and needed replays before next time gets played.
 */
public class QueueSong extends Song {

    private List<Vote> votes = new ArrayList<>();
    private int playsBeforeReplay;
    private int id;

    public QueueSong() {
        super();
    }

    /**
     * Set {@link #votes} to zero/empty and set {@link #id} and {@link #playsBeforeReplay}.
     *
     * @param song copy song
     * @param id   id of Song
     * @param playsBeforeReplay counter how many songs must be played before replay
     */
    public QueueSong(final Song song, final int id, final int playsBeforeReplay) {
        super(song);
        this.id = id;
        this.playsBeforeReplay = playsBeforeReplay;
    }

    /**
     * Copy Constructor.
     * @param queueSong queueSong to copy
     */
    public QueueSong(final QueueSong queueSong) {
        this(queueSong, queueSong.getId(), queueSong.getPlaysBeforeReplay());
    }

    public List<Vote> getVotes() {
        return votes;
    }

    @JsonIgnore
    public int getVoteCount() {
        return votes.size();
    }

    public int getPlaysBeforeReplay() {
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

    void increaseVote(final User user) {
        votes.add(new Vote(user));
    }

    void resetVote() {
        votes.clear();
    }


    public void setPlaysBeforeReplay(final int playsBeforeReplay) {
        this.playsBeforeReplay = playsBeforeReplay;
    }

    @Override
    public String toString() {
        return "voteCount=" + votes.toString()
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
        return id == queueSong.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
