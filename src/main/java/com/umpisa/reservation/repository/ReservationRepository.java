package com.umpisa.reservation.repository;

import com.umpisa.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    @Modifying
    @Query("UPDATE Reservation SET reservationDateTime = :reservationDateTime" +
            ", numGuests = :numGuests WHERE reservationId = :reservationId")
    void updateReservationTimeAndNumberOfGuest(@Param(value = "reservationId") UUID reservationID,
                                               @Param(value = "reservationDateTime") LocalDateTime reservationDateTime,
                                               @Param(value = "numGuests") int numGuests);

    @Query(value = "SELECT * FROM Reservation " +
            "WHERE DATEDIFF('second', CURRENT_TIMESTAMP(), RESERVATION_DATE_TIME) >= :lowerLimit ", nativeQuery = true)
    List<Reservation> getTobeRemindedReservations(@Param(value = "lowerLimit") long limit);
}
