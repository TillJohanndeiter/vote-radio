package de.techfak.gse.tjohanndeiter.controller;

import de.techfak.gse.tjohanndeiter.model.client.Client;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class VolumeController implements PropertyChangeListener {

    public static final int MAX_VOLUME = 100;
    @FXML
    private Slider volumeSlider = new Slider();

    private MusicPlayer musicPlayer;

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        if (event.getPropertyName().equals(MusicPlayer.VOLUME_CHANGED)) {
            int volume = (Integer) event.getNewValue();
            volumeSlider.setValue(volume);
        } else if (event.getPropertyName().equals(Client.NEW_PLAYER)) {
            musicPlayer = (MusicPlayer) event.getNewValue();
        }
    }

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


    public void changeVolume() {
        musicPlayer.setVolume((int) volumeSlider.getValue());
        volumeSlider.setMin(0);
        volumeSlider.setMax(MAX_VOLUME);
    }


}
