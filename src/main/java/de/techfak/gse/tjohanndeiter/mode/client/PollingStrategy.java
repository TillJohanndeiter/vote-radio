package de.techfak.gse.tjohanndeiter.mode.client;

import de.techfak.gse.tjohanndeiter.json.JsonException;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import de.techfak.gse.tjohanndeiter.model.playlist.VotedSong;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * UpdateStrategy use simple polling to rest server. Every five seconds client make request to server.
 * @deprecated because sockets are way better.
 */
@Deprecated
public class PollingStrategy implements UpdateStrategy {

    private static final int REQUEST_PERIOD = 5000;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final HttpRequester requester;

    private VotedSong oldSong;
    private VoteList oldList;

    private Timer currentSongTimer;
    private Timer playlistTimer;

    PollingStrategy(final HttpRequester requester) {
        this.requester = requester;
        startRequestCurrentSong();
        startRequestPlaylist();
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener observer) {
        support.addPropertyChangeListener(observer);
    }

    private void startRequestCurrentSong() {

        currentSongTimer = new Timer();

        final TimerTask currentSongRequester = new TimerTask() {
            @Override
            public void run() {
                try {
                    final VotedSong currentSong = requester.getCurrentSong().getQueueSong();
                    support.firePropertyChange(MusicPlayer.NEW_SONG, oldSong, currentSong);
                    support.firePropertyChange(Client.CONNECTED, true, true);
                    oldSong = currentSong;
                } catch (JsonException e) {
                    support.firePropertyChange(JSON_ERROR, null, null);
                } catch (IOException | InterruptedException e) {
                    support.firePropertyChange(LOST_CONNECTION, null, null);
                }
            }
        };
        currentSongTimer.schedule(currentSongRequester, 0, REQUEST_PERIOD);
    }

    private void startRequestPlaylist() {

        playlistTimer = new Timer();

        final TimerTask playlistRequester = new TimerTask() {
            @Override
            public void run() {
                try {
                    final VoteList voteList = requester.getPlaylist();
                    support.firePropertyChange(Playlist.PLAYLIST_CHANGE, oldList, voteList);
                    support.firePropertyChange(Client.CONNECTED, false, true);
                    oldList = voteList;
                } catch (JsonException e) {
                    support.firePropertyChange(JSON_ERROR, null, null);
                } catch (IOException | InterruptedException e) {
                    support.firePropertyChange(LOST_CONNECTION, null, null);
                }
            }
        };

        playlistTimer.schedule(playlistRequester, 0, REQUEST_PERIOD);

    }

    @Override
    public void stop() {
        if (playlistTimer != null) {
            playlistTimer.cancel();
        }

        if (currentSongTimer != null) {
            currentSongTimer.cancel();
        }
    }
}
