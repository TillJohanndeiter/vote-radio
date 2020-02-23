package de.techfak.gse.tjohanndeiter.mode.client;

import de.techfak.gse.tjohanndeiter.controller.CurrentSongController;
import de.techfak.gse.tjohanndeiter.controller.NetworkController;
import de.techfak.gse.tjohanndeiter.controller.TableController;
import de.techfak.gse.tjohanndeiter.controller.VolumeController;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.model.client.Client;
import de.techfak.gse.tjohanndeiter.model.server.User;
import de.techfak.gse.tjohanndeiter.model.voting.ClientStrategy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientMode extends Application implements ProgramMode {


    @Override
    public void start(final Stage stage) throws IOException {

        final Client client = new Client();
        final ClientStrategy clientStrategy = new ClientStrategy(client);

        final FXMLLoader networkFxmlLoader = new FXMLLoader(Thread.currentThread().
                getContextClassLoader().getResource("NetworkControlPanel.fxml"));
        final Pane networkRoot = networkFxmlLoader.load();
        final NetworkController networkController = networkFxmlLoader.getController();
        final FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().
                getContextClassLoader().getResource("Table.fxml"));
        final Pane root = fxmlLoader.load();
        final TableController tableController = fxmlLoader.getController();
        tableController.init(clientStrategy, User.CLIENT);
        tableController.setControlPane(networkRoot);
        client.addPropertyChangeListener(tableController);
        networkController.init(client);

        final FXMLLoader currentSongLoader = new FXMLLoader(Thread.currentThread().
                getContextClassLoader().getResource("CurrentSong.fxml"));
        final Pane currentSongPane = currentSongLoader.load();
        final CurrentSongController currentSongController = currentSongLoader.getController();
        tableController.setCurrentSongPane(currentSongPane);
        client.addPropertyChangeListener(currentSongController);

        final FXMLLoader volumePanelLoader = new FXMLLoader(Thread.currentThread().
                getContextClassLoader().getResource("VolumePanel.fxml"));
        final Pane volumePane = volumePanelLoader.load();
        final VolumeController volumeController = volumePanelLoader.getController();
        tableController.setVolumePane(volumePane);
        volumeController.init(client.getReceiverPlayer());
        client.addPropertyChangeListener(volumeController);


        final Scene scene = new Scene(root);
        stage.setOnCloseRequest(windowEvent -> {
            client.kill();
            currentSongController.end();
            Platform.exit();
        });
        stage.setTitle("GSERadio Client mode");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void startProgram() {
        Application.launch();
    }
}
