package de.techfak.gse.tjohanndeiter.controller;

import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class JukeBoxController implements PropertyChangeListener {

    //TODO: Icons for stop and play

    private static final String PAUSE_BUTTON_TEXT = "Pause";

    @FXML
    private Button pauseOrResumeButton = new Button();

    private MusicPlayer musicPlayer;

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        switch (event.getPropertyName()) { //NOPMD
            case MusicPlayer.PAUSE_PLAYER:
                pauseOrResumeButton.setText("Resume");
                break;
            case MusicPlayer.RESUME_PLAYER:
                pauseOrResumeButton.setText(PAUSE_BUTTON_TEXT);
                break;
            default:
        }
    }


    public void init(final MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    @FXML
    public void startPlay() {
        musicPlayer.startPlay();
    }

    @FXML
    public void stopPlay() {
        musicPlayer.stop();
    }

    /**
     * Set the play and resume button to pause or resume.
     */
    @FXML
    public void pauseOrResume() {
        musicPlayer.changePlayingState();
    }
}
