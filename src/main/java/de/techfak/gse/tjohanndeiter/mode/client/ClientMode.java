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

/**
 * Program mode as client. Use java fx for graphic presentation.
 */
public class ClientMode extends Application implements ProgramMode {


    private static final int MIN_HEIGHT = 610;
    private static final int MIN_WIDTH = 1000;

    @Override
    public void start(final Stage stage) throws IOException {

        final Client client = new Client();
        final ClientStrategy clientStrategy = new ClientStrategy(client);

        final Pane networkRoot = setUpNetworkController(client);

        final FXMLLoader fxmlLoader = new FXMLLoader(Thread.currentThread().
                getContextClassLoader().getResource("Table.fxml"));
        final Pane root = fxmlLoader.load();
        final TableController tableController = fxmlLoader.getController();
        tableController.init(clientStrategy, User.getClient());
        tableController.setControlPane(networkRoot);


        client.addPropertyChangeListener(tableController);

        final CurrentSongController currentSongController = setUpCurrentSongController(client, tableController);
        setUpVolumeController(client, tableController);
        final Scene scene = new Scene(root);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setMinWidth(MIN_WIDTH);
        stage.setOnCloseRequest(windowEvent -> {
            client.kill();
            currentSongController.end();
            Platform.exit();
        });
        stage.setTitle("GSERadio Client mode");
        stage.setScene(scene);
        stage.show();
    }

    private Pane setUpNetworkController(final Client client) throws IOException {
        final FXMLLoader networkFxmlLoader = new FXMLLoader(Thread.currentThread().
                getContextClassLoader().getResource("NetworkControlPanel.fxml"));
        final Pane networkRoot = networkFxmlLoader.load();
        final NetworkController networkController = networkFxmlLoader.getController();
        networkController.init(client);
        return networkRoot;
    }

    private void setUpVolumeController(final Client client, final TableController tableController)
            throws IOException {
        final FXMLLoader volumePanelLoader = new FXMLLoader(Thread.currentThread().
                getContextClassLoader().getResource("VolumePanel.fxml"));
        final Pane volumePane = volumePanelLoader.load();
        final VolumeController volumeController = volumePanelLoader.getController();
        tableController.setVolumePane(volumePane);
        volumeController.init(client.getReceiverPlayer());
        client.addPropertyChangeListener(volumeController);
    }

    private CurrentSongController setUpCurrentSongController(final Client client, final TableController tableController)
            throws IOException {
        final FXMLLoader currentSongLoader = new FXMLLoader(Thread.currentThread().
                getContextClassLoader().getResource("CurrentSong.fxml"));
        final Pane currentSongPane = currentSongLoader.load();
        final CurrentSongController currentSongController = currentSongLoader.getController();
        tableController.setCurrentSongPane(currentSongPane);
        client.addPropertyChangeListener(currentSongController);
        return currentSongController;
    }

    @Override
    public void startProgram() {
        Application.launch();
    }
}
