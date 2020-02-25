package de.techfak.gse.tjohanndeiter.mode.server;


/**
 * Bean for {@link #multicastAddress} and {@link #port} for multicast stream of
 * {@link de.techfak.gse.tjohanndeiter.model.player.StreamPlayer}.
 */
public class StreamUrl {

    private final String multicastAddress;
    private final String port;

    StreamUrl(final String multicastAddress, final int port) {
        this.multicastAddress = multicastAddress;
        this.port = String.valueOf(port);
    }

    StreamUrl(final String multicastAddress, final String port) {
        this.multicastAddress = multicastAddress;
        this.port = port;
    }

    public String getMulticastAddress() {
        return multicastAddress;
    }

    public String getPort() {
        return port;
    }
}
