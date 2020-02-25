package de.techfak.gse.tjohanndeiter.mode.client;

import de.techfak.gse.tjohanndeiter.json.SongUploadJSonParser;
import de.techfak.gse.tjohanndeiter.json.SongUploadParser;
import de.techfak.gse.tjohanndeiter.mode.server.SessionHandler;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.Base64;


/**
 * Responsible for song uploads to server form client.
 */
public class Uploader {

    public static final String SUCCESS_UPLOAD = "SUCCESS_UPLOAD";
    public static final String FAILED_UPLOAD = "FAILED_UPLOAD";
    private static final int OK_CODE = 200;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final SongUploadJSonParser songUploadJSonParser = new SongUploadParser();

    public Uploader(final PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }


    /**
     * Upload mp3 file represented by #file to server with #ipAddress and #restPort.
     *
     * @param file      file to upload
     * @param ipAddress address of server
     * @param restPort  port of server
     */
    public void uploadAndCheckSuccess(final File file, final String ipAddress, final String restPort) {
        try {
            if (fileIsValidAndExits(file)) {
                final String json = getJson(file);
                final HttpRequest httpRequest = createRequest(ipAddress, restPort, json);
                uploadAndCheckSuccess(file, httpRequest);
            }
        } catch (IOException | InterruptedException e) {
            support.firePropertyChange(FAILED_UPLOAD, null, file);
        }
    }

    private String getJson(final File file) throws IOException {
        final byte[] fileData = Files.readAllBytes(file.toPath());
        final String encoded = Base64.getEncoder().encodeToString(fileData);
        final SongUpload songUpload = new SongUpload(encoded, file.getName());
        return songUploadJSonParser.toJson(songUpload);
    }


    private HttpRequest createRequest(final String ipAddress, final String restPort, final String json) {
        return HttpRequest.newBuilder().uri(
                URI.create("http://" + ipAddress + ':' + restPort + SessionHandler.UPLOAD_FILE)).
                POST(HttpRequest.BodyPublishers.ofString(json)).build();
    }

    private void uploadAndCheckSuccess(final File file, final HttpRequest httpRequest)
            throws IOException, InterruptedException {
        final HttpClient client = HttpClient.newHttpClient();
        if (OK_CODE == client.send(httpRequest, HttpResponse.BodyHandlers.ofString()).statusCode()) {
            support.firePropertyChange(SUCCESS_UPLOAD, null, file.getName());
        } else {
            support.firePropertyChange(FAILED_UPLOAD, null, file);
        }
    }

    private boolean fileIsValidAndExits(final File file) {
        return file != null && file.exists() && file.getAbsolutePath().toLowerCase().endsWith(".mp3"); //NOPMD
    }
}
