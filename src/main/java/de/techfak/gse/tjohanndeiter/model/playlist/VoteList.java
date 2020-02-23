package de.techfak.gse.tjohanndeiter.model.playlist;

import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.database.SongLibrary;
import de.techfak.gse.tjohanndeiter.model.exception.database.SongIdNotAvailable;
import de.techfak.gse.tjohanndeiter.model.server.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class VoteList extends Playlist {

    private int playsBeforeReplay;
    private QueueSong currentSong;
    private LinkedList<QueueSong> songList = new LinkedList<>();

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
     * @param songLibrary songLibrary contains all {@link de.techfak.gse.tjohanndeiter.model.database.Song}
     *                    used in playlist.
     */
    public VoteList(final SongLibrary songLibrary, final int playsBeforeReplay) {
        super();
        this.playsBeforeReplay = playsBeforeReplay;
        int idx = 0;
        for (final Song song : songLibrary.getSongs()) {
            final QueueSong votedSong = new QueueSong(song, idx, 0); //NOPMD
            songList.add(votedSong);
            idx++;
        }
        currentSong = songList.get(0);
    }


    /**
     * Only for tests
     * @param songList test list
     */

    public VoteList(final List<QueueSong> songList) {
        super();
        for (final QueueSong song : songList) {
            final QueueSong votedSong = new QueueSong(song); //NOPMD
            this.songList.add(votedSong);
        }
    }

    /**
     * Vote for song by his id.
     *
     * @param idOfSong idOfSong
     * @throws SongIdNotAvailable in case if not song with #idOfSong is available
     */
    public void voteForSongById(final int idOfSong, final User user) throws SongIdNotAvailable {
        QueueSong foundSong = findSongById(idOfSong);
        updatePlaylist(foundSong, user);
    }

    private void updatePlaylist(final QueueSong foundSong, final User user) {
        if (!currentSong.equals(foundSong)) {
            foundSong.increaseVote(user);
            songList.sort(new VoteComparator());
            correction();
            support.firePropertyChange(PLAYLIST_CHANGE, null, this);
        }
    }


    public QueueSong findSongById(final int idOfSong) throws SongIdNotAvailable {
        for (final QueueSong votedSong : songList) {
            if (votedSong.getId() == idOfSong) {
                return votedSong;
            }
        }
        throw new SongIdNotAvailable(idOfSong);
    }


    private void correction() {
        for (QueueSong queueSong : songList) {
                while (isNotHighest(queueSong) && couldBeHigher(queueSong) && nextSongLessVotes(queueSong)) {
                    forwardSwap(queueSong);
                }
        }
    }

    private boolean couldBeHigher(final QueueSong queueSong) {
        return queueSong.getPlaysBeforeReplay() < songList.indexOf(queueSong);
    }

    private boolean isNotHighest(final QueueSong queueSong) {
        return songList.indexOf(queueSong) > 1;
    }

    private void forwardSwap(final QueueSong queueSong) {
        final int pos = songList.indexOf(queueSong);
        Collections.swap(songList, pos, pos - 1);
    }

    private boolean nextSongLessVotes(final QueueSong queueSong) {
        return queueSong.getVoteCount() > songList.get(songList.indexOf(queueSong) - 1).getVoteCount();
    }

    @Override
    public void addSong(final Song song) {
        songList.addLast(new QueueSong(song, songList.size(), playsBeforeReplay));
        support.firePropertyChange(PLAYLIST_CHANGE, null, this);
    }

    @Override
    public void skipToNext() {
        final QueueSong old = songList.get(0);
        old.setPlaysBeforeReplay(playsBeforeReplay);
        old.resetVote();
        songList.remove(old);
        songList.add(old);
        currentSong = songList.get(0);
        support.firePropertyChange(PLAYLIST_CHANGE, null, this);
        for (int i = 0; i < songList.size() - 1; i++) {
            songList.get(i).decReplayCount();
        }
        songList.sort(new VoteComparator());
    }

    public List<QueueSong> getVotedPlaylist() {
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

    class VoteComparator implements Comparator<QueueSong> {

        @Override
        public int compare(final QueueSong queueSong, final QueueSong t1) {
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
