package com.umpisa.reservation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reservationId;
    @NotNull
    private String name;
    @NotNull
    private String phoneNumber;
    @NotNull
    @Email
    private String email;
    @Min(1)
    private int numGuests;
    private LocalDateTime reservationDateTime;
    private CommunicationMethod preferredCommunicationMethod;

    public UUID getReservationId() {
        return reservationId;
    }

    public void setReservationId(UUID reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public void setReservationDateTime(LocalDateTime reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public CommunicationMethod getPreferredCommunicationMethod() {
        return preferredCommunicationMethod;
    }

    public void setPreferredCommunicationMethod(CommunicationMethod preferredCommunicationMethod) {
        this.preferredCommunicationMethod = preferredCommunicationMethod;
    }
}
