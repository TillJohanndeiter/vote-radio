package de.techfak.gse.tjohanndeiter.model.player;

import de.techfak.gse.tjohanndeiter.model.database.Song;
import de.techfak.gse.tjohanndeiter.model.playlist.Playlist;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Abstract representations of a musicPlayer. Use VlcJ Library for play music itself.
 */
public abstract class MusicPlayer {

    public static final String END_PLAYER = "endPlayer";
    public static final String PAUSE_PLAYER = "pausePlayer";
    public static final String RESUME_PLAYER = "resumePlayer";
    public static final String START_PLAYER = "startPlayer";
    public static final String NEW_SONG = "newSong";
    public static final String VOLUME_CHANGED = "volumeChanged";

    MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
    MediaPlayer mediaPlayer = mediaPlayerFactory.mediaPlayers().newMediaPlayer();
    PropertyChangeSupport support = new PropertyChangeSupport(this);
    Playlist playlist;

    MusicPlayer(final Playlist playlist) {
        this.playlist = playlist;
    }

    /**
     * Start the music with current/first song in {@link #playlist} and inform observers about event.
     */
    public void startPlay() {
        final Song song = playlist.getCurrentSong();
        support.firePropertyChange(NEW_SONG, null, song);
        support.firePropertyChange(Playlist.PLAYLIST_CHANGE, null, playlist);
        mediaPlayer.controls().play();
        support.firePropertyChange(START_PLAYER, !mediaPlayer.status().isPlaying(),
                mediaPlayer.status().isPlaying());
    }

    /**
     * Ends the music and releases all c bindings.
     */
    public void end() {
        mediaPlayer.controls().stop();
        mediaPlayerFactory.release();
        mediaPlayer.release();
    }

    /**
     * Stop the music and informs observers about event.
     */
    public void stop() {
        mediaPlayer.controls().stop();
        support.firePropertyChange(END_PLAYER, mediaPlayer.status().isPlaying(),
                !mediaPlayer.status().isPlaying());
    }


    /**
     * Stop music if is currently playing otherwise it will start playing. Method is an interface for controllers to
     * keep low-coupling between controller and model.
     */
    public void changePlayingState() {
        if (mediaPlayer.status().isPlaying()) {
            pause();
        } else if (!mediaPlayer.status().isPlayable()) {
            startPlay();
        } else {
            resume();
        }
    }

    public TimeBean createPlayTimeBean() {
        return new TimeBean(mediaPlayer.status().length(), mediaPlayer.status().time());
    }

    /**
     * Change volume of music.
     * @param volume new volume to set
     */
    public void setVolume(final int volume) {
        mediaPlayer.audio().setVolume(volume);
        support.firePropertyChange(VOLUME_CHANGED, null, mediaPlayer.audio().volume());
    }

    public void addPropertyChangeListener(final PropertyChangeListener observer) {
        support.addPropertyChangeListener(observer);
    }

    private void resume() {
        mediaPlayer.controls().play();
        support.firePropertyChange(RESUME_PLAYER, null, createPlayTimeBean());
    }

    private void pause() {
        mediaPlayer.controls().pause();
        support.firePropertyChange(PAUSE_PLAYER, mediaPlayer.status().isPlaying(),
                !mediaPlayer.status().isPlaying());
    }

    class EndEvent implements Runnable {

        @Override
        public void run() {
            playlist.skipToNext();
            final Song song = playlist.getCurrentSong();
            support.firePropertyChange(NEW_SONG, null, song);
            System.out.println("test");
        }
    }
}
