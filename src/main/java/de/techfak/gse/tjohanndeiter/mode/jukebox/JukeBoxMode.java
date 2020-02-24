package de.techfak.gse.tjohanndeiter.mode.jukebox;

import de.techfak.gse.tjohanndeiter.controller.CurrentSongController;
import de.techfak.gse.tjohanndeiter.controller.JukeBoxController;
import de.techfak.gse.tjohanndeiter.controller.TableController;
import de.techfak.gse.tjohanndeiter.controller.VolumeController;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import de.techfak.gse.tjohanndeiter.model.server.User;
import de.techfak.gse.tjohanndeiter.model.voting.VoteStrategy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Program mode as offline jukebox. Use java fx for graphic presentation.
 */
public class JukeBoxMode extends Application implements ProgramMode {


    private static final int MIN_HEIGHT = 610;
    private static final int MIN_WIDTH = 950;

    private static VoteList voteList;
    private static MusicPlayer musicPlayer;
    private static VoteStrategy voteStrategy;

    public JukeBoxMode() {
    }

    public static void setVoteList(final VoteList voteList) {
        JukeBoxMode.voteList = voteList;
    }

    public static void setMusicPlayer(final MusicPlayer musicPlayer) {
        JukeBoxMode.musicPlayer = musicPlayer;
    }

    public static void setVoteStrategy(final VoteStrategy voteStrategy) {
        JukeBoxMode.voteStrategy = voteStrategy;
    }

    @Override
    public void startProgram() {
        //TODO: Implement choose of playsBeforeReplay
        Application.launch();
    }

    @Override
    public void start(final Stage stage) throws IOException {
        final FXMLLoader fxmlLoader = getFXMLLoader("Table.fxml");
        final Pane root = fxmlLoader.load();
        final TableController tableController = setUpTableController(fxmlLoader);
        setUpOfflineController(tableController);
        final CurrentSongController currentSongController = setUpCurrentSongController(tableController);
        setUpVolumeController(tableController);
        showScene(stage, root, currentSongController);
    }

    private void showScene(final Stage stage, final Pane root, final CurrentSongController currentSongController) {
        final Scene scene = new Scene(root);
        stage.setOnCloseRequest(windowEvent -> {
            musicPlayer.end();
            currentSongController.end();
            Platform.exit();
        });
        stage.setTitle("JukeBox Mode");
        stage.setScene(scene);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setMinWidth(MIN_WIDTH);
        stage.show();
    }

    private TableController setUpTableController(final FXMLLoader fxmlLoader) {
        final TableController tableController = fxmlLoader.getController();
        tableController.init(voteStrategy, User.getJukebox());
        musicPlayer.addPropertyChangeListener(tableController);
        voteList.addPropertyChangeListener(tableController);
        return tableController;
    }

    private void setUpOfflineController(final TableController tableController) throws IOException {
        final FXMLLoader controlPanelLoader = getFXMLLoader("OfflineControlPanel.fxml");
        final Pane panelRoot = controlPanelLoader.load();
        final JukeBoxController jukeBoxController = controlPanelLoader.getController();
        jukeBoxController.init(musicPlayer, voteList);
        musicPlayer.addPropertyChangeListener(jukeBoxController);
        tableController.setControlPane(panelRoot);
    }

    private CurrentSongController setUpCurrentSongController(final TableController tableController) throws IOException {
        final FXMLLoader currentSongLoader = getFXMLLoader("CurrentSong.fxml");
        final Pane currentSongPane = currentSongLoader.load();
        final CurrentSongController currentSongController = currentSongLoader.getController();
        tableController.setCurrentSongPane(currentSongPane);
        musicPlayer.addPropertyChangeListener(currentSongController);
        voteList.addPropertyChangeListener(currentSongController);
        return currentSongController;
    }

    private void setUpVolumeController(final TableController tableController) throws IOException {
        final FXMLLoader volumePanelLoader = getFXMLLoader("VolumePanel.fxml");
        final Pane volumePane = volumePanelLoader.load();
        final VolumeController volumeController = volumePanelLoader.getController();
        tableController.setVolumePane(volumePane);
        volumeController.init(musicPlayer);
        musicPlayer.addPropertyChangeListener(volumeController);
    }

    private FXMLLoader getFXMLLoader(final String s) {
        return new FXMLLoader(Thread.currentThread().
                getContextClassLoader().getResource(s));
    }
}
