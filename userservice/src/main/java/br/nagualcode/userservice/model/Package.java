package br.nagualcode.userservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "packages", schema = "userservice")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String trackingNumber;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Package() {}

    public Package(String trackingNumber, String status) {
        this.trackingNumber = trackingNumber;
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

    
    // Getters and setters
}

