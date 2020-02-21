package de.techfak.gse.tjohanndeiter.model.player;

import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Abstract representations of a musicPlayer.
 */
public abstract class MusicPlayer {

    public static final String END_PLAYER = "endPlayer";
    public static final String PAUSE_PLAYER = "pausePlayer";
    public static final String RESUME_PLAYER = "resumePlayer";
    public static final String START_PLAYER = "startPlayer";
    public static final String NEW_SONG = "NEW_SONG";

    MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
    MediaPlayer mediaPlayer = mediaPlayerFactory.mediaPlayers().newMediaPlayer();
    PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    Playlist playlist;

    MusicPlayer(final Playlist playlist) {
        this.playlist = playlist;
    }

    public void startPlay() {
        final Song song = playlist.getCurrentSong();
        propertyChangeSupport.firePropertyChange(NEW_SONG, null, song);
        propertyChangeSupport.firePropertyChange(Playlist.PLAYLIST_CHANGE, null, playlist);
        mediaPlayer.controls().play();
        propertyChangeSupport.firePropertyChange(START_PLAYER, !mediaPlayer.status().isPlaying(),
                mediaPlayer.status().isPlaying());
    }

    public void end() {
        mediaPlayer.controls().stop();
        mediaPlayerFactory.release();
        mediaPlayer.release();
    }

    public void stop() {
        mediaPlayer.controls().stop();
        propertyChangeSupport.firePropertyChange(END_PLAYER, mediaPlayer.status().isPlaying(),
                !mediaPlayer.status().isPlaying());
    }

    public void addPropertyChangeListener(final PropertyChangeListener observer) {
        propertyChangeSupport.addPropertyChangeListener(observer);
    }


    public void changePlayingState() {
        if (mediaPlayer.status().isPlaying()) {
            pause();
        } else {
            resume();
        }
    }

    public TimeBean createPlayTimeBean() {
        return new TimeBean(mediaPlayer.status().length(), mediaPlayer.status().time());
    }

    void afterSongEvent() {
        playlist.skipToNext();
        final Song song = playlist.getCurrentSong();
        propertyChangeSupport.firePropertyChange(NEW_SONG, null, song);
    }

    private void resume() {
        mediaPlayer.controls().play();
        propertyChangeSupport.firePropertyChange(RESUME_PLAYER, null, createPlayTimeBean());
    }

    private void pause() {
        mediaPlayer.controls().pause();
        propertyChangeSupport.firePropertyChange(PAUSE_PLAYER, mediaPlayer.status().isPlaying(),
                !mediaPlayer.status().isPlaying());
    }

    class EndEvent implements Runnable {

        @Override
        public void run() {
            playlist.skipToNext();
            final Song song = playlist.getCurrentSong();
            propertyChangeSupport.firePropertyChange(NEW_SONG, null, song);
        }
    }
}
