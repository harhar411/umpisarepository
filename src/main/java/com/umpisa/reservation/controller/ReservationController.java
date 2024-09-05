package com.umpisa.reservation.controller;

import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.umpisa.reservation.dto.ReservationTimeAndNumberOfGuestsDto;
import com.umpisa.reservation.dto.ResponseDto;
import com.umpisa.reservation.entity.CommunicationMethod;
import com.umpisa.reservation.entity.Reservation;
import com.umpisa.reservation.service.ReservationService;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JavaMailSender emailSender;

    @Value("${twilio.SID}")
    private String twilioSID;

    @Value("${twilio.auth}")
    private String twilioAuth;

    @Value("${twilio.phone.num}")
    private String twilioPhone;

    @PostMapping(path = "/createReservation", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createReservation(@RequestBody Reservation reservation) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime rDate = reservation.getReservationDateTime().truncatedTo(ChronoUnit.MINUTES);

        if(rDate.compareTo(now) < 0)
            return new ResponseEntity<>(new ResponseDto("Reservation date is invalid"), HttpStatus.OK);

        try {
            InternetAddress ia = new InternetAddress(reservation.getEmail());
            ia.validate();
        }
        catch (AddressException e) {
            return new ResponseEntity<>(new ResponseDto("Invalid email address: " + reservation.getEmail()),
                    HttpStatus.OK);
        }

        if(!reservation.getPhoneNumber().matches("^\\+[0-9]{12}"))
            return new ResponseEntity<>(new ResponseDto("Invalid phone number: " + reservation.getPhoneNumber()),
                    HttpStatus.OK);

        reservation.setReservationDateTime(rDate);
        Reservation newReservation = reservationService.createReservation(reservation);

        if(newReservation.getPreferredCommunicationMethod().equals(CommunicationMethod.EMAIL)) {
            try {
                sendSTMPMessage(reservation);
            }
            catch (MailException me) {
                return new ResponseEntity<>(new ResponseDto("Unable to send an email: " + me.getMessage()),
                        HttpStatus.OK);
            }
        }
        else {
            sendSMS(reservation);
        }

        return new ResponseEntity<>(newReservation, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getReservations() {
        return new ResponseEntity<>(reservationService.getAllReservations(), HttpStatus.OK);
    }

    @GetMapping(path = "/cancelReservation")
    public ResponseEntity<Object> cancelReservation(@RequestParam("reservationId") UUID uuid) {
        reservationService.cancelReservation(uuid);
        return new ResponseEntity<>(new ResponseDto("Reservation of id : " + uuid + " has been cancelled."), HttpStatus.OK);
    }

    @PutMapping(path = "/updateReservationTimeAndNumberOfGuest")
    public ResponseEntity<Object> updateReservationTimeAndNumberOfGuest(@RequestBody ReservationTimeAndNumberOfGuestsDto reservation) {
        Reservation original = reservationService.getAReservation(reservation.getReservationId());
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime rDate = reservation.getReservationDateTime().truncatedTo(ChronoUnit.MINUTES);

        if(rDate.compareTo(now) < 0)
            return new ResponseEntity<>(new ResponseDto("Reservation date is invalid"), HttpStatus.OK);

        if(reservation.getNumGuests() <= 0)
            return new ResponseEntity<>(new ResponseDto("Reservation numGuests is invalid"), HttpStatus.OK);

        return new ResponseEntity<>(reservationService.updateReservation(
                reservation.convertToReservation(original.getName(),
                        original.getPhoneNumber(),
                        original.getEmail())), HttpStatus.OK);
    }

    @GetMapping(path = "/time")
    public ResponseEntity<LocalDateTime> justGetTime() {
        TwilioRestClient trc = new TwilioRestClient.Builder(twilioSID, twilioAuth).build();

        Message.creator(new PhoneNumber("+639218556418"),
                new PhoneNumber(twilioPhone), "Thank you for reserving with us.\n" +
                        "\n" +
                        "Thank you for you patronage!!!").create(trc);

        return new ResponseEntity<>(LocalDateTime.now(), HttpStatus.OK);
    }

    private void sendSTMPMessage(Reservation reservation) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("esoen0919clarence212@gmail.com");
        message.setTo(reservation.getEmail());
        message.setSubject("Reservation: " + reservation.getReservationId());
        message.setText("Thank for your patronage!!!");
        emailSender.send(message);
    }

    private void sendSMS(Reservation reservation) {
        TwilioRestClient trc = new TwilioRestClient.Builder(twilioSID, twilioAuth).build();

        Message.creator(new PhoneNumber("+639218556418"),
                new PhoneNumber(twilioPhone), "Thank you for reserving with us.\n" +
                        "\n" +
                        "Thank you for you patronage!!!").create(trc);
    }
}
