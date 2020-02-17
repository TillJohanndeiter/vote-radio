package de.techfak.gse.tjohanndeiter.model.player;

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

    private MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
    private PropertyChangeSupport propertyChangeSupport;
    MediaPlayer mediaPlayer = mediaPlayerFactory.mediaPlayers().newMediaPlayer();

    MusicPlayer() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void startPlay() {
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

    private void resume() {
        mediaPlayer.controls().play();
        propertyChangeSupport.firePropertyChange(RESUME_PLAYER, !mediaPlayer.status().isPlaying(),
                mediaPlayer.status().isPlaying());
    }

    private void pause() {
        mediaPlayer.controls().pause();
        propertyChangeSupport.firePropertyChange(PAUSE_PLAYER, mediaPlayer.status().isPlaying(),
                !mediaPlayer.status().isPlaying());
    }
}
