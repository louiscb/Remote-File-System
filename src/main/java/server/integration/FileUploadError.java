package server.integration;

public class FileUploadError extends Exception {
    public FileUploadError() {
        super("File didn't upload");
    }
}