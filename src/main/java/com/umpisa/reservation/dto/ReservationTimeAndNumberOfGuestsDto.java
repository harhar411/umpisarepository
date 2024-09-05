package com.umpisa.reservation.dto;

import com.umpisa.reservation.entity.Reservation;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReservationTimeAndNumberOfGuestsDto {
    private UUID reservationId;
    private int numGuests;
    private LocalDateTime reservationDateTime;

    public ReservationTimeAndNumberOfGuestsDto(UUID reservationId, int numGuests, LocalDateTime reservationDateTime) {
        this.reservationId = reservationId;
        this.numGuests = numGuests;
        this.reservationDateTime = reservationDateTime;
    }

    public Reservation convertToReservation(String name, String phoneNumber, String email) {
        Reservation reservation = new Reservation();
        reservation.setName(name);
        reservation.setPhoneNumber(phoneNumber);
        reservation.setEmail(email);
        reservation.setReservationId(this.reservationId);
        reservation.setReservationDateTime(this.reservationDateTime);
        reservation.setNumGuests(this.numGuests);

        return reservation;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public void setReservationId(UUID reservationId) {
        this.reservationId = reservationId;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public LocalDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public void setReservationDateTime(LocalDateTime reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }
}
