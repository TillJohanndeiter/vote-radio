package de.tjohanndeiter.controller;

import de.tjohanndeiter.mode.client.Client;
import de.tjohanndeiter.mode.client.UpdateStrategy;
import de.tjohanndeiter.mode.client.Uploader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;


/**
 * Controller responsible for user interaction to connect or disconnect client to server.
 */
public class NetworkController implements PropertyChangeListener { //NOPMD

    private static final Color NOT_CONNECTED_COLOR = Color.web("#c9c9c9");
    private static final Color RECONNECT_COLOR = Color.web("#ffff97");
    private static final Color CONNECTED_COLOR = Color.web("#c3fb8a");
    private static final Color ERROR_COLOR = Color.web("#fb9898");

    private static final String DISCONNECT = "Disconnect";
    private static final String CONNECT = "Connect";
    private static final String NOT_CONNECTED = "Not Connected";

    @FXML
    private TextField ipAddressField = new TextField();

    @FXML
    private TextField restPortField = new TextField();

    @FXML
    private Rectangle colorNetStatus = new Rectangle();

    @FXML
    private Text networkTextStatus = new Text();

    @FXML
    private Button connectButton = new Button();

    private Uploader uploader = new Uploader(this);

    private Client client;

    @Override
    public void propertyChange(final PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case Client.INVALID_URL:
                showInvalidURL();
                break;
            case Client.INVALID_SERVER:
                showInvalidServer();
                break;
            case Client.CONNECTED:
                Platform.runLater(this::connectedNotify);
                break;
            case UpdateStrategy.LOST_CONNECTION:
                Platform.runLater(this::lostConnectionNotify);
                break;
            case Client.CANCELED_CONNECTION:
                Platform.runLater(this::disconnectNotify);
                break;
            case UpdateStrategy.JSON_ERROR:
                Platform.runLater(this::jsonErrorNotify);
                break;
            case Uploader.SUCCESS_UPLOAD:
                Platform.runLater(this::successUploadNotify);
                break;
            case Uploader.FAILED_UPLOAD:
                Platform.runLater(this::failedUploadNotify);
                break;
            default:
                break;
        }
    }

    @FXML
    public void doConnectionChange() {
        client.changeConnection(ipAddressField.getText(), restPortField.getText());
    }


    /**
     * Opens file chooser and give file to {@link Uploader} to make and upload.
     */
    @FXML
    public void uploadFile() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Files to SongUpload");
        fileChooser.setInitialFileName(".mp3");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3", "*.mp3"));
        fileChooser.setInitialDirectory(new File("/"));
        final File file = fileChooser.showOpenDialog(null);
        uploader.uploadAndCheckSuccess(file, ipAddressField.getText(), restPortField.getText());
    }

    //TODO: Implement methods

    private void failedUploadNotify() {

    }

    private void successUploadNotify() {

    }


    private void showInvalidURL() {
        networkTextStatus.setText("Invalid URL!");
        connectButton.setText(CONNECT);
        colorNetStatus.setFill(ERROR_COLOR);
    }

    private void showInvalidServer() {
        networkTextStatus.setText("Invalid Server");
        connectButton.setText(CONNECT);
        colorNetStatus.setFill(ERROR_COLOR);
    }

    private void disconnectNotify() {
        networkTextStatus.setText(NOT_CONNECTED);
        connectButton.setText(CONNECT);
        colorNetStatus.setFill(NOT_CONNECTED_COLOR);
    }

    private void lostConnectionNotify() {
        networkTextStatus.setText("Try Reconnect");
        connectButton.setText(DISCONNECT);
        colorNetStatus.setFill(RECONNECT_COLOR);
    }

    private void connectedNotify() {
        networkTextStatus.setText(CONNECT);
        connectButton.setText(DISCONNECT);
        colorNetStatus.setFill(CONNECTED_COLOR);
    }

    private void jsonErrorNotify() {
        networkTextStatus.setText("Json Error");
        connectButton.setText(DISCONNECT);
        colorNetStatus.setFill(ERROR_COLOR);
    }

    /**
     * Initialize default values for localhost and port 8080.
     * @param client client used for server communication
     */
    public void init(final Client client) {
        ipAddressField.setText("127.0.0.1"); //NOPMD
        restPortField.setText("8080");
        colorNetStatus.setFill(NOT_CONNECTED_COLOR);
        this.client = client;
        this.client.addPropertyChangeListener(this);
    }
}
