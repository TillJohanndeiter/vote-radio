package de.techfak.gse.tjohanndeiter.model.server;


/**
 * Bean for {@link #multicastAddress} and {@link #port} for multicast stream of
 * {@link de.techfak.gse.tjohanndeiter.model.player.StreamPlayer}.
 */
public class StreamUrl {

    private final String multicastAddress;
    private final String port;

    public StreamUrl(final String multicastAddress, final int port) {
        this.multicastAddress = multicastAddress;
        this.port = String.valueOf(port);
    }

    public StreamUrl(final String multicastAddress, final String port) {
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
