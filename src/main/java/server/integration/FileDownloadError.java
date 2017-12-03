package server.integration;

public class FileDownloadError extends Exception {
    public FileDownloadError() {
        super("File didn't download");
    }
}