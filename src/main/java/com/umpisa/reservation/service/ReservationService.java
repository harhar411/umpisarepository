package com.umpisa.reservation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.umpisa.reservation.entity.Reservation;

public interface ReservationService {
    public Reservation createReservation(Reservation r);
    public void cancelReservation(UUID reservationId);
    public List<Reservation> getAllReservations();
    public Reservation updateReservation(Reservation r);
    public Reservation getAReservation(UUID reservationId);
}
