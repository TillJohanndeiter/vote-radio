package de.techfak.gse.tjohanndeiter.model.playlist;

import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.database.SongLibrary;
import de.techfak.gse.tjohanndeiter.model.exception.database.SongIdNotAvailable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class VoteList extends Playlist {


    public static final String VOTE_CHANGED = "VOTE_CHANGED";


    private List<QueueSong> songList = new ArrayList<>();
    private int playsBeforeReplay;

    /**
     * Default constructor for serialization. Do not delete.
     */
    private VoteList() {
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
    }


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
    public void voteForSongById(final int idOfSong) throws SongIdNotAvailable {
        boolean found = false;
        for (final QueueSong votedSong : songList) {
            if (votedSong.getId() == idOfSong) {
                voteForSong(votedSong);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new SongIdNotAvailable(idOfSong);
        }
    }

    /**
     * Increase vote for #votedSong and update list {@link #songList}.
     *
     * @param votedSong voted Song.
     */
    private void voteForSong(final QueueSong votedSong) {
        votedSong.increaseVote();
        while (isNotHighestVoted(songList.indexOf(votedSong))
                && allowedReplay(votedSong) && nextSongLessVotes(votedSong)) {
            forwardSwap(votedSong);
        }
        correctNextPlay();
        propertyChangeSupport.firePropertyChange(PLAYLIST_CHANGE, null, this);
    }

    private boolean allowedReplay(final QueueSong queueSong) {
        return queueSong.getPlaysBeforeReplay() < songList.indexOf(queueSong);
    }

    private void backwardSwap(final QueueSong votedSong) {
        Collections.swap(songList, songList.indexOf(votedSong), songList.indexOf(votedSong) + 1);
    }

    private void forwardSwap(final QueueSong votedSong) {
        Collections.swap(songList, songList.indexOf(votedSong), songList.indexOf(votedSong) - 1);
    }

    private boolean nextSongLessVotes(final QueueSong votedSong) {
        return getNext(votedSong).getVoteCount() < votedSong.getVoteCount();
    }

    private QueueSong getNext(final QueueSong queueSong) {
        return songList.get(songList.indexOf(queueSong) - 1);
    }

    private boolean isNotHighestVoted(final int oldIdxOfSong) {
        return oldIdxOfSong > 0;
    }

    @Override
    public void addSong(final Song song) {
        songList.add(new QueueSong(song, songList.size(), playsBeforeReplay));
    }

    @Override
    public QueueSong getNextSong() {
        final QueueSong song = songList.get(0);
        song.setPlaysBeforeReplay(playsBeforeReplay);
        song.resetVote();
        propertyChangeSupport.firePropertyChange(NEW_SONG, songList.get(songList.size() - 1), song);
        songList.remove(song);
        songList.add(song);
        propertyChangeSupport.firePropertyChange(PLAYLIST_CHANGE, null, this);
        for (int i = 0; i < songList.size() - 1; i++) {
            songList.get(i).decReplayCount();
        }
        correctNextPlay();
        return song;
    }

    private void correctNextPlay() {
        for (int i = 0; i < songList.size() - 2; i++) {
            QueueSong temp = songList.get(i);
            while (temp.getVoteCount() < songList.get(songList.indexOf(temp) + 1).getVoteCount() && allowedReplay(songList.get(songList.indexOf(temp) + 1))) {
                backwardSwap(temp);
            }
        }
    }

    @Override
    public QueueSong getCurrentSong() {
        propertyChangeSupport.firePropertyChange(
                NEW_SONG, null, songList.get(0));
        propertyChangeSupport.firePropertyChange(PLAYLIST_CHANGE, null, this);
        return songList.get(songList.size() - 1);
    }

    public List<QueueSong> getVotedPlaylist() {
        return songList;
    }

    @Override
    public String toString() {
        // final StringBuilder result = new StringBuilder();
        // result.append("Current Song: \n");
        // result.append(getNextSong().toString()).append('\n');
        // result.append('\n');
        // for (int i = 0; i < songList.size() - 1; i++) {
        //     result.append(songList.get(i).toString()).append('\n');
        // }
        // result.append("End of Playlist\n");
        return null;
    }
}
