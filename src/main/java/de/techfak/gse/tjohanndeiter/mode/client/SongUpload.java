package de.techfak.gse.tjohanndeiter.mode.client;


/**
 * Bean for song upload. Contains filename and encoded base64 string.
 */
public class SongUpload {

    private String base64Code;
    private String fileName;

    private SongUpload() {
    }

    SongUpload(final String base64Code, final String fileName) {
        this.base64Code = base64Code;
        this.fileName = fileName;
    }

    public String getBase64Code() {
        return base64Code;
    }

    public String getFileName() {
        return fileName;
    }
}
