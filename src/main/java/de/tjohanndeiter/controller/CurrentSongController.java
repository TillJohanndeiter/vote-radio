package de.tjohanndeiter.controller;

import de.tjohanndeiter.mode.client.Client;
import de.tjohanndeiter.mode.server.CurrentSong;
import de.tjohanndeiter.model.database.Song;
import de.tjohanndeiter.model.player.MusicPlayer;
import de.tjohanndeiter.model.player.TimeBean;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Responsible for refreshing metadata and cover of current song.
 */
public class CurrentSongController implements PropertyChangeListener {

    private static final String DEFAULT_COVER = "/blankCdIcon.png";
    private static final int PERIOD = 20;
    private static final String DEFAULT_TIME_STAMP = "0:00";

    @FXML
    private Text title = new Text();

    @FXML
    private Text artist = new Text();

    @FXML
    private Text album = new Text();

    @FXML
    private Text duration = new Text();

    @FXML
    private Text playedTime = new Text();

    @FXML
    private ImageView cover;

    @FXML
    private Slider timeSlider = new Slider();

    private TimerTask task;
    private Timer timer;


    public void initialize() {
        cover.setImage(new Image(DEFAULT_COVER));
    }

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case MusicPlayer.END_PLAYER:
                resetCurrentSong();
                break;
            case MusicPlayer.PAUSE_PLAYER:
                task.cancel();
                break;
            case MusicPlayer.RESUME_PLAYER:
                final TimeBean timeBean = (TimeBean) event.getNewValue();
                startTimeCounter(timeBean);
                break;
            case MusicPlayer.NEW_SONG:
                final Song newSong = (Song) event.getNewValue();
                Platform.runLater(() -> updateCurrentSong(newSong, new TimeBean(newSong.getLength(), 0)));
                break;
            case Client.NEW_SONG:
                final CurrentSong response = (CurrentSong) event.getNewValue();
                final Song song = response.getQueueSong();
                updateCurrentSong(song, response.getTimeBean());
                startTimeCounter(response.getTimeBean());
                break;
            default:
                break;
        }
    }

    private void resetCurrentSong() {
        title.setText(null);
        artist.setText(null);
        duration.setText(null);
        album.setText(null);
        cover.setImage(new Image(DEFAULT_COVER));
        resetTime();
    }

    private void updateCurrentSong(final Song song, final TimeBean timeBean) {
        updateMetaDataOfCurrentSong(song);
        setCover(song);
        setInitialTime(song);
        startTimeCounter(timeBean);
    }

    private void updateMetaDataOfCurrentSong(final Song newSong) {
        title.setText(newSong.getTitle());
        artist.setText(newSong.getArtist());
        album.setText(newSong.getAlbum());
    }

    private void setCover(final Song newSong) {
        if (newSong.getCover() == null) {
            cover.setImage(new Image(DEFAULT_COVER));
        } else {
            cover.setImage(new Image(new ByteArrayInputStream(newSong.getCover())));
        }
    }

    private void setInitialTime(final Song newSong) {
        final String timeFormat = ControllerUtils.generateTimeFormat(newSong.getLength());
        duration.setText(timeFormat);
        playedTime.setText(timeFormat);
    }

    private void startTimeCounter(final TimeBean timeBean) {
        startTimeCounter(timeBean.getLength(), timeBean.getPlayedTime());
    }

    private void startTimeCounter(final long lengthOfSong, final long played) {
        resetTime();
        timeSlider.setMax(lengthOfSong);
        timeSlider.setValue(played);
        task = new TimerTask() {
            long counter = played;

            @Override
            public void run() {
                if (counter <= lengthOfSong) {
                    playedTime.setText(ControllerUtils.generateTimeFormat(lengthOfSong - counter));
                    timeSlider.setValue(timeSlider.getMin() + counter);
                    counter = counter + PERIOD;
                }
            }
        };
        timer = new Timer();
        timer.schedule(task, PERIOD, PERIOD);
        task.run();
    }

    private void resetTime() {
        if (task != null) {
            task.cancel();
        }
        playedTime.setText(DEFAULT_TIME_STAMP);
        timeSlider.setValue(timeSlider.getMin());
    }


    /**
     * Kills the timer task and the task itself.
     */
    public void end() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        if (task != null) {
            task.cancel();
        }
    }
}
