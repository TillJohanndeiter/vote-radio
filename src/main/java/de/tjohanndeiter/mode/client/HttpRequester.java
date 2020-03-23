package de.tjohanndeiter.mode.client;

import de.tjohanndeiter.json.CurrentSongJsonParser;
import de.tjohanndeiter.json.CurrentSongJsonParserImpl;
import de.tjohanndeiter.json.JsonException;
import de.tjohanndeiter.json.StreamUrlJsonParser;
import de.tjohanndeiter.json.StreamUrlJsonParserImpl;
import de.tjohanndeiter.json.UserJsonParser;
import de.tjohanndeiter.json.UserJsonParserImpl;
import de.tjohanndeiter.json.VoteListJsonParser;
import de.tjohanndeiter.json.VoteListJsonParserImpl;
import de.tjohanndeiter.mode.server.CurrentSong;
import de.tjohanndeiter.mode.server.SessionHandler;
import de.tjohanndeiter.mode.server.StreamUrl;
import de.tjohanndeiter.mode.server.User;
import de.tjohanndeiter.model.playlist.VoteList;

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

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final CurrentSongJsonParser currentSongJsonParser = new CurrentSongJsonParserImpl();
    private final VoteListJsonParser voteListJsonParser = new VoteListJsonParserImpl();
    private final UserJsonParser userJsonParser = new UserJsonParserImpl();
    private final StreamUrlJsonParser streamAddressJsonParser = new StreamUrlJsonParserImpl();

    private String restAddress;
    private String port;


    HttpRequester(final String restAddress, final String port) {
        this.restAddress = restAddress;
        this.port = port;
    }

    public StreamUrl getStreamUrl() throws IOException, InterruptedException, JsonException {
        return streamAddressJsonParser.toCurrentSongResponse(getRequestBody(SessionHandler.STREAMING_ADDRESS));
    }

    String getRestAddress() {
        return restAddress;
    }

    String getPort() {
        return port;
    }

    VoteList getPlaylist() throws IOException, JsonException, InterruptedException {
        return voteListJsonParser.toPlaylist(getJsonString(SessionHandler.PLAYLIST));
    }


    CurrentSong getCurrentSong() throws IOException, InterruptedException, JsonException {
        return currentSongJsonParser.toCurrentSongResponse(getJsonString(SessionHandler.CURRENT_SONG));
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

    private String getRequestBody(final String subAddress) throws IOException, InterruptedException {
        final HttpRequest httpRequest = getRequest(subAddress);
        final HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    int voteById(final int id) throws IOException, InterruptedException {
        final HttpRequest httpRequest = getRequest("/?" + SessionHandler.VOTED_SONG_PARAM + "=" + id);
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).statusCode();
    }

    User getUser() throws IOException, InterruptedException, JsonException {
        return userJsonParser.toUser(getRequestBody(SessionHandler.USER));
    }

    private String getJsonString(final String subAddress) throws IOException, InterruptedException {
        final HttpRequest httpRequest = HttpRequest.newBuilder().uri(
                URI.create(HTTP + restAddress + ':' + port + subAddress)).GET().build();
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
    }
}
