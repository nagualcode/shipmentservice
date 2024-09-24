package br.nagualcode.trackingservice.exception;

public class PackageNotFoundException extends RuntimeException {
    public PackageNotFoundException(String message) {
        super(message);
    }
}
