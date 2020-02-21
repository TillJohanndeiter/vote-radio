package de.techfak.gse.tjohanndeiter.model.server;

public class StreamUrl {

    private String multicastAddress;
    private String port;

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
