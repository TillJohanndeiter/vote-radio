package de.tjohanndeiter.exception.shutdown;

public class VlcNotFoundException extends VlcJException {

    private static final long serialVersionUID = 1L;

    public VlcNotFoundException() {
        super("VlcJ NativeDiscovery failed to load Vlc. Please reinstall vlc on your computer");
    }
}
