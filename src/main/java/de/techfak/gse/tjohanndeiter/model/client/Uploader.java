package de.techfak.gse.tjohanndeiter.model.client;

import de.techfak.gse.tjohanndeiter.model.json.SongUploadJSonParser;
import de.techfak.gse.tjohanndeiter.model.json.SongUploadParser;
import de.techfak.gse.tjohanndeiter.model.server.SessionHandler;

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

public class Uploader {

    public static final String SUCCESS_UPLOAD = "SUCCESS_UPLOAD";
    public static final String FAILED_UPLOAD = "FAILED_UPLOAD";
    private static final int OK_CODE = 200;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private SongUploadJSonParser jSonParser = new SongUploadParser();

    public Uploader(final PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void upload(final File file, final String ipAddress, final String restPort)
            throws IOException, InterruptedException {

        if (file != null && file.getAbsolutePath().toLowerCase().endsWith(".mp3")) {

            final byte[] fileData = Files.readAllBytes(file.toPath());
            final String encoded = Base64.getEncoder().encodeToString(fileData);
            final HttpClient client = HttpClient.newHttpClient();
            final SongUpload songUpload = new SongUpload(encoded, file.getName());
            final String json = jSonParser.toJson(songUpload);

            final HttpRequest httpRequest = HttpRequest.newBuilder().uri(
                    URI.create("http://" + ipAddress + ':' + restPort + SessionHandler.UPLOAD_FILE)).
                    POST(HttpRequest.BodyPublishers.ofString(json)).build();

            if (OK_CODE == client.send(httpRequest, HttpResponse.BodyHandlers.ofString()).statusCode()) {
                support.firePropertyChange(SUCCESS_UPLOAD, null, file.getName());
            } else {
                support.firePropertyChange(FAILED_UPLOAD, null, file);
            }
        }
    }
}
