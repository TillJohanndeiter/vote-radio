package de.techfak.gse.tjohanndeiter.controller;

import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;


/**
 * Responsible for user interaction in offline/jukebox mode so to start and end music.
 */
public class JukeBoxController implements PropertyChangeListener {

    private static final Image PAUSE_ICON = new Image("/pause.png");
    private static final Image PLAY_ICON = new Image("/play.png");

    @FXML
    private ImageView startOrResume;

    @FXML
    private ChoiceBox<Integer> playsForReplayChoice = new ChoiceBox<>();

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
     *
     * @param musicPlayer musicPlayer to set
     */
    public void init(final MusicPlayer musicPlayer, final VoteList voteList) {
        this.musicPlayer = musicPlayer;
        final List<Integer> nums = new ArrayList<>();
        for (int i = 0; i < voteList.getVotedPlaylist().size(); i++) {
            nums.add(i);
        }
        playsForReplayChoice.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1)
                -> voteList.setNeededReplays(t1.intValue()));
        playsForReplayChoice.setItems(FXCollections.observableArrayList(nums));
        playsForReplayChoice.setValue(0);
        playsForReplayChoice.setTooltip(new Tooltip("Select plays before replay"));
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
