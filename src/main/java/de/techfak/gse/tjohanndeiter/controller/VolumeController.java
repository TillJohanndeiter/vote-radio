package de.techfak.gse.tjohanndeiter.controller;

import de.techfak.gse.tjohanndeiter.model.client.Client;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * Responsible for user interaction referring the sound of played music. So to make it louder or less.
 */
public class VolumeController implements PropertyChangeListener {

    public static final int MAX_VOLUME = 100;
    @FXML
    private final Slider volumeSlider = new Slider();

    private MusicPlayer musicPlayer;

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        if (event.getPropertyName().equals(MusicPlayer.VOLUME_CHANGED)) {
            final int volume = (Integer) event.getNewValue();
            volumeSlider.setValue(volume);
        } else if (event.getPropertyName().equals(Client.NEW_PLAYER)) {
            musicPlayer = (MusicPlayer) event.getNewValue();
        }
    }


    /**
     * Initialize {@link #musicPlayer} and set up event if {@link #volumeSlider} is changed.
     * @param mp musicPlayer to set
     */
    public void init(final MusicPlayer mp) {
        this.musicPlayer = mp;
        volumeSlider.valueProperty().addListener((observableValue, number, t1) -> {
                if (musicPlayer != null) {
                musicPlayer.setVolume(number.intValue());
            }
        }
        );
        volumeSlider.setValue(MAX_VOLUME);
    }
}
