package de.techfak.gse.tjohanndeiter.model.client;

import de.techfak.gse.tjohanndeiter.model.json.QueueSongJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.QueueSongJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.json.TimeBeanJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.TimeBeanJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.json.UserJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.UserJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.json.VoteListJsonParser;
import de.techfak.gse.tjohanndeiter.model.json.VoteListJsonParserImpl;
import de.techfak.gse.tjohanndeiter.model.player.TimeBean;
import de.techfak.gse.tjohanndeiter.model.playlist.QueueSong;
import de.techfak.gse.tjohanndeiter.model.playlist.VoteList;
import de.techfak.gse.tjohanndeiter.model.server.SessionHandler;
import de.techfak.gse.tjohanndeiter.model.server.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Responsible for make http rest requests to the server and parse them from json to the object.
 */
class HttpRequester {

    private static final String HTTP = "http://";
    private static final int STATUS_OK = 200;

    private HttpClient httpClient = HttpClient.newHttpClient();
    private QueueSongJsonParser songJsonParser = new QueueSongJsonParserImpl();
    private VoteListJsonParser voteListJsonParser = new VoteListJsonParserImpl();
    private TimeBeanJsonParser timeBeanJsonParser = new TimeBeanJsonParserImpl();
    private UserJsonParser userJsonParser = new UserJsonParserImpl();

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
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
    }

    VoteList getPlaylist() throws IOException, InterruptedException {
        return voteListJsonParser.toPlaylist(getJsonString(SessionHandler.PLAYLIST));
    }


    QueueSong getCurrentSong() throws IOException, InterruptedException {
        return songJsonParser.toSong(getJsonString(SessionHandler.CURRENT_SONG));
    }

    boolean validConnection() throws IOException, InterruptedException, IllegalArgumentException {
        final HttpRequest httpRequest = getRequest(SessionHandler.HANDSHAKE);
        final HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
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
        final HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    int voteById(final int id) throws IOException, InterruptedException {
        final HttpRequest httpRequest = getRequest("/?" + SessionHandler.VOTED_SONG_PARAM + "=" + id);
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).statusCode();
    }

    public TimeBean getPlayedTime() throws IOException, InterruptedException {
        return timeBeanJsonParser.toTimeBean(getRequestBody(SessionHandler.PLAY_TIME));
    }

    User getUser() throws IOException, InterruptedException {
        return userJsonParser.toUser(getRequestBody(SessionHandler.USER));
    }
}
