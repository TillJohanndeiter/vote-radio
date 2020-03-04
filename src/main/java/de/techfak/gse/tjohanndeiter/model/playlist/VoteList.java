package de.techfak.gse.tjohanndeiter.model.playlist;

import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.database.SongLibrary;
import de.techfak.gse.tjohanndeiter.exception.database.SongIdNotAvailable;
import de.techfak.gse.tjohanndeiter.mode.server.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class VoteList extends Playlist {

    private final LinkedList<VotedSong> songList = new LinkedList<>();
    private VoteComparator voteComparator = new VoteComparator();
    private int playsBeforeReplay;
    private VotedSong currentSong;

    /**
     * Default constructor for serialization. Do not delete.
     */
    private VoteList() {
        super();
    }

    /**
     * Make a save copy of {@link de.techfak.gse.tjohanndeiter.model.database.Song} in #songLibrary.
     * Set up {@link #songList} with ids.
     *
     * @param songLibrary       songLibrary contains all {@link de.techfak.gse.tjohanndeiter.model.database.Song}
     *                          used in playlist.
     * @param playsBeforeReplay counter of how many songs have to be played minimum after a replay.
     */
    public VoteList(final SongLibrary songLibrary, final int playsBeforeReplay) {
        super();
        this.playsBeforeReplay = playsBeforeReplay;
        int idx = 0;
        for (final Song song : songLibrary.getSongs()) {
            final VotedSong votedSong = new VotedSong(song, idx, 0); //NOPMD
            songList.add(votedSong);
            idx++;
        }
        currentSong = songList.get(0);
    }



    /**
     * Only for tests.
     *
     * @param songList test list
     */

    public VoteList(final List<VotedSong> songList) {
        super();
        for (final VotedSong song : songList) {
            final VotedSong votedSong = new VotedSong(song); //NOPMD
            this.songList.add(votedSong);
        }
    }

    public void setNeededReplays(final int newReplays) {
        for (final VotedSong queueSong : songList) {
            if (queueSong.getPlaysBeforeReplay() > newReplays) {
                queueSong.setPlaysBeforeReplay(newReplays);
            }
        }
        playsBeforeReplay = newReplays;
        songList.sort(voteComparator);
        correction();
        support.firePropertyChange(PLAYLIST_CHANGE, null, this);
    }

    /**
     * Vote for song by his id. Then resort it with {@link VoteComparator}, correct order and notify observers.
     *
     * @param idOfSong idOfSong
     * @param user user who created vote
     * @throws SongIdNotAvailable in case if not song with #idOfSong is available
     */
    public void voteForSongById(final int idOfSong, final User user) throws SongIdNotAvailable {
        final VotedSong foundSong = findSongById(idOfSong);
        updatePlaylist(foundSong, user);
    }


    private void updatePlaylist(final VotedSong foundSong, final User user) {
        if (!currentSong.equals(foundSong)) {
            foundSong.increaseVote(user);
            songList.sort(voteComparator);
            correction();
            support.firePropertyChange(PLAYLIST_CHANGE, null, this);
        }
    }


    /**
     * Returns {@link VotedSong} by id.
     * @param idOfSong id to lookup
     * @return song by #idOfSong
     * @throws SongIdNotAvailable in case of id that doesn't belong to any song
     */
    public VotedSong findSongById(final int idOfSong) throws SongIdNotAvailable {
        for (final VotedSong votedSong : songList) {
            if (votedSong.getId() == idOfSong) {
                return votedSong;
            }
        }
        throw new SongIdNotAvailable(idOfSong);
    }


    /**
     * After list get resorted by {@link VoteComparator} songs with less votes could be ranked higher caused by
     * playedBeforeReplay. Method bring it back to the correct order.
     */
    private void correction() {
        for (final VotedSong queueSong : songList) {
            while (isNotHighest(queueSong) && couldBeHigher(queueSong) && nextSongLessVotes(queueSong)) {
                forwardSwap(queueSong);
            }
        }
    }

    private boolean couldBeHigher(final VotedSong queueSong) {
        return queueSong.getPlaysBeforeReplay() < songList.indexOf(queueSong);
    }

    private boolean isNotHighest(final VotedSong queueSong) {
        return songList.indexOf(queueSong) > 1;
    }

    private void forwardSwap(final VotedSong queueSong) {
        final int pos = songList.indexOf(queueSong);
        Collections.swap(songList, pos, pos - 1);
    }

    private boolean nextSongLessVotes(final VotedSong queueSong) {
        return queueSong.getVoteCount() > songList.get(songList.indexOf(queueSong) - 1).getVoteCount();
    }

    @Override
    public void addSong(final Song song) {
        songList.addLast(new VotedSong(song, songList.size(), playsBeforeReplay));
        support.firePropertyChange(PLAYLIST_CHANGE, null, this);
    }

    /**
     * Put first song to last position in {@link #songList}, reset Votes and decrease playsBeforeReplay for every
     * {@link Song} in {@link #songList} then resort {@link #songList}.
     */
    @Override
    public void skipToNext() {
        final VotedSong old = songList.get(0);
        old.setPlaysBeforeReplay(playsBeforeReplay);
        old.resetVote();
        songList.remove(old);
        songList.add(old);
        currentSong = songList.get(0);
        support.firePropertyChange(PLAYLIST_CHANGE, null, this);
        for (int i = 0; i < songList.size() - 1; i++) {
            songList.get(i).decReplayCount();
        }
        songList.sort(voteComparator);
        correction();
    }

    public List<VotedSong> getVotedPlaylist() {
        return songList;
    }

    @Override
    public Song getCurrentSong() {
        return songList.get(0);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder(" --Start of playlist");
        for (final Song song : songList) {
            result.append(song.toString()).append('\n');
        }
        result.append(" -- End of playlist");
        return result.toString();
    }

    class VoteComparator implements Comparator<VotedSong> {

        @Override
        public int compare(final VotedSong queueSong, final VotedSong t1) {
            if (t1.equals(currentSong)) {
                return Integer.MAX_VALUE;
            } else if (songList.indexOf(t1) <= queueSong.getPlaysBeforeReplay()) {
                return Integer.MAX_VALUE;
            } else {
                return t1.getVoteCount() - queueSong.getVoteCount();
            }
        }
    }
}
