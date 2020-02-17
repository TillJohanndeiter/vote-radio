package de.techfak.gse.tjohanndeiter.mode.client;

import de.techfak.gse.tjohanndeiter.controller.NetworkController;
import de.techfak.gse.tjohanndeiter.mode.ProgramMode;
import de.techfak.gse.tjohanndeiter.model.client.Client;
import de.techfak.gse.tjohanndeiter.model.voting.ClientStrategy;
import de.techfak.gse.tjohanndeiter.controller.TableController;
import javafx.application.Application;
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
        tableController.init(clientStrategy);
        tableController.changePanelPane(networkRoot);
        client.addPropertyChangeListener(tableController);
        networkController.init(client);
        final Scene scene = new Scene(root);
        stage.setOnCloseRequest(windowEvent -> client.endConnection());
        stage.setTitle("GSERadio Client mode");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void startProgram() {
        Application.launch();
    }
}
