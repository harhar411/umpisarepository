package com.umpisa.reservation.service;

import com.umpisa.reservation.entity.Reservation;
import com.umpisa.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public Reservation createReservation(Reservation r) {
        return reservationRepository.save(r);
    }

    @Override
    public void cancelReservation(UUID reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation updateReservation(Reservation r) {
        return reservationRepository.save(r);
    }

    @Override
    public Reservation getAReservation(UUID reservationId) {
        return reservationRepository.findById(reservationId).get();
    }

}
