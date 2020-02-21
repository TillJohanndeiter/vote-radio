package de.techfak.gse.tjohanndeiter.mode.jukebox;

import de.techfak.gse.tjohanndeiter.controller.CurrentSongController;
import de.techfak.gse.tjohanndeiter.controller.JukeBoxController;
import de.techfak.gse.tjohanndeiter.controller.TableController;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.model.database.SongLibrary;
import de.techfak.gse.tjohanndeiter.model.database.SongLibraryFactory;
import de.techfak.gse.tjohanndeiter.model.database.SongLibraryVlcJFactory;
import de.techfak.gse.tjohanndeiter.model.exception.prototypes.ShutdownException;
import de.techfak.gse.tjohanndeiter.model.player.MusicPlayer;
import de.techfak.gse.tjohanndeiter.model.player.OfflinePlayer;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import de.techfak.gse.tjohanndeiter.model.voting.JukeBoxStrategy;
import de.techfak.gse.tjohanndeiter.model.voting.VoteStrategy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class JukeBoxMode extends Application implements ProgramMode {

    private static VoteList voteList;
    private static MusicPlayer musicPlayer;
    private static VoteStrategy voteStrategy;


    private static String filepath;

    public JukeBoxMode() {

    }

    //TODO: change to static setter instead of rape constructor

    JukeBoxMode(final String fp) {
        filepath = fp;
    }

    @Override
    public void start(final Stage stage) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().
                getContextClassLoader().getResource("Table.fxml"));
        final Pane root = fxmlLoader.load();
        final TableController tableController = fxmlLoader.getController();
        tableController.init(voteStrategy);
        musicPlayer.addPropertyChangeListener(tableController);
        voteList.addPropertyChangeListener(tableController);

        final FXMLLoader controlPanelLoader = new FXMLLoader(Thread.currentThread().
                getContextClassLoader().getResource("OfflineControlPanel.fxml"));
        final Pane panelRoot = controlPanelLoader.load();
        final JukeBoxController jukeBoxController = controlPanelLoader.getController();
        jukeBoxController.init(musicPlayer);
        tableController.setControlPane(panelRoot);


        final FXMLLoader currentSongLoader = new FXMLLoader(Thread.currentThread().
                getContextClassLoader().getResource("CurrentSong.fxml"));
        final Pane currentSongPane = currentSongLoader.load();
        final CurrentSongController  currentSongController = currentSongLoader.getController();
        tableController.setCurrentSongPane(currentSongPane);
        musicPlayer.addPropertyChangeListener(currentSongController);
        voteList.addPropertyChangeListener(currentSongController);

        final Scene scene = new Scene(root);
        stage.setOnCloseRequest(windowEvent -> {
            musicPlayer.end();
            currentSongController.end();
        });
        stage.setTitle("JukeBox Mode");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void startProgram() throws ShutdownException {
        final SongLibraryFactory songLibraryFactory = new SongLibraryVlcJFactory();
        final SongLibrary songLibrary = songLibraryFactory.createSongLibrary(new File(filepath));
        voteList = new VoteList(songLibrary, 1);
        voteStrategy = new JukeBoxStrategy(voteList);
        musicPlayer = new OfflinePlayer(voteList);
        Application.launch();
    }
}
