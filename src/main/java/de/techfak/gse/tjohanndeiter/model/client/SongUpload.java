package de.techfak.gse.tjohanndeiter.model.client;

public class SongUpload {

    private String base64Code;
    private String name;

    private SongUpload() {
    }

    SongUpload(final String base64Code, final String name) {
        this.base64Code = base64Code;
        this.name = name;
    }

    public String getBase64Code() {
        return base64Code;
    }

    public String getName() {
        return name;
    }
}
