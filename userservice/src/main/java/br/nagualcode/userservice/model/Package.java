package br.nagualcode.userservice.model;

import br.nagualcode.userservice.client.TrackingServiceClient;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "packages", schema = "userservice")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String trackingNumber;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Transient 
    private TrackingServiceClient trackingServiceClient;

    public Package() {}

    public Package(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Package(String trackingNumber, TrackingServiceClient trackingServiceClient) {
        this.trackingNumber = trackingNumber;
        this.trackingServiceClient = trackingServiceClient;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTrackingServiceClient(TrackingServiceClient trackingServiceClient) {
        this.trackingServiceClient = trackingServiceClient;
    }

    @Override
    public String toString() {
        // Faz a chamada ao TrackingService para obter o status atual do pacote
        try {
            String status = trackingServiceClient.getStatus(this.trackingNumber);
            return "Package{trackingNumber='" + trackingNumber + "', status='" + status + "'}";
        } catch (Exception e) {
            return "Package{trackingNumber='" + trackingNumber + "', status='Unknown'}";
        }
    }
}
