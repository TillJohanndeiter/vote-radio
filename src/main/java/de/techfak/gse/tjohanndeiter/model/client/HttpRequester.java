package de.techfak.gse.tjohanndeiter.model.client;

import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.json.QueueSongJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.QueueSongJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.json.VoteListJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.VoteListJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import de.techfak.gse.tjohanndeiter.model.server.SessionHandler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class HttpRequester {

    private static final String HTTP = "http://";
    private static final int STATUS_OK = 200;

    private HttpClient client = HttpClient.newHttpClient();
    private QueueSongJsonParser songJsonParser = new QueueSongJsonParserImpl();
    private VoteListJsonParser voteListJsonParser = new VoteListJsonParserImpl();

    private String restAddress;
    private String port;


    HttpRequester(final String restAddress, final String port) {
        this.restAddress = restAddress;
        this.port = port;
    }

    String getRestAddress() {
        return restAddress;
    }

    String getPort() {
        return port;
    }

    private String getJsonString(final String subAddress) throws IOException, InterruptedException {
        final HttpRequest httpRequest = HttpRequest.newBuilder().uri(
                URI.create(HTTP + restAddress + ':' + port + subAddress)).GET().build();
        return client.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
    }

    VoteList getPlaylist() throws IOException, InterruptedException {
        return voteListJsonParser.toPlaylist(getJsonString(SessionHandler.PLAYLIST));
    }


    QueueSong getCurrentSong() throws IOException, InterruptedException {
        return songJsonParser.toSong(getJsonString(SessionHandler.CURRENT_SONG));
    }

    boolean validConnection() throws IOException, InterruptedException, IllegalArgumentException {
        final HttpRequest httpRequest = getRequest(SessionHandler.HANDSHAKE);
        final HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == STATUS_OK && response.body().equals(SessionHandler.RESPONSE_HANDSHAKE);
    }

    private HttpRequest getRequest(final String uri) {
        return HttpRequest.newBuilder().uri(
                URI.create(HTTP + restAddress + ':' + port + uri)).
                GET().build();
    }

    String getMusicPort() throws IOException, InterruptedException, IllegalArgumentException {
        return getRequestBody(SessionHandler.STREAMING_PORT);
    }

    String getMusicAddress() throws IOException, InterruptedException {
        return getRequestBody(SessionHandler.STREAMING_ADDRESS);
    }

    private String getRequestBody(final String subAddress) throws IOException, InterruptedException {
        final HttpRequest httpRequest = getRequest(subAddress);
        final HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    void voteById(final int id) throws IOException, InterruptedException {
        final HttpRequest httpRequest = getRequest("/?" + SessionHandler.VOTED_SONG_PARAM + "=" + id);
        client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }
}
