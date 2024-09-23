package br.nagualcode.notificationservice.model;

public class Package {
    private String trackingNumber;
    private String status;

    public Package() {}

    public Package(String trackingNumber, String status) {
        this.trackingNumber = trackingNumber;
        this.status = status;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
