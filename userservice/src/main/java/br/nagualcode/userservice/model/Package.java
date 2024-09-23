package br.nagualcode.userservice.model;

import jakarta.persistence.*;

@Entity
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String trackingNumber;

    // toString method to call the TrackingService
    @Override
    public String toString() {
        // OpenFeign will be used to query the status from trackingservice
        return "Package: " + this.trackingNumber;  // We will implement the Feign client later
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

    // Getters and Setters
}
