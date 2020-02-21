package de.techfak.gse.tjohanndeiter.model.server;

public class StreamUrl {

    private String multicastAddress;
    private int port;

    public StreamUrl(final String multicastAddress, final int port) {
        this.multicastAddress = multicastAddress;
        this.port = port;
    }

    public String getMulticastAddress() {
        return multicastAddress;
    }

    public int getPort() {
        return port;
    }
}
