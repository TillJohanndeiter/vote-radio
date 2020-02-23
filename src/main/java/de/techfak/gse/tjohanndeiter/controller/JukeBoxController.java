package de.techfak.gse.tjohanndeiter.controller;

import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * Responsible for user interaction in offline/jukebox mode so to start and end music.
 */
public class JukeBoxController implements PropertyChangeListener {

    private static final Image PAUSE_ICON = new Image("/pause.png");
    private static final Image PLAY_ICON = new Image("/play.png");

    @FXML
    private ImageView startOrResume;

    private MusicPlayer musicPlayer;

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        switch (event.getPropertyName()) { //NOPMD
            case MusicPlayer.END_PLAYER:
            case MusicPlayer.PAUSE_PLAYER:
                startOrResume.setImage(PLAY_ICON);
                break;
            case MusicPlayer.RESUME_PLAYER:
            case MusicPlayer.START_PLAYER:
                startOrResume.setImage(PAUSE_ICON);
                break;
            default:
                break;
        }
    }

    /**
     * Initialize the controller and set music player.
     * @param musicPlayer musicPlayer to set
     */
    public void init(final MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    @FXML
    public void stopPlay() {
        musicPlayer.stop();
    }

    /**
     * Set the play and resume button to pause or resume.
     */
    @FXML
    public void changePlayState() {
        musicPlayer.changePlayingState();
    }
}
