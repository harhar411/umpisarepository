package com.umpisa.reservation.scheduledtask;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.umpisa.reservation.dto.ResponseDto;
import com.umpisa.reservation.entity.CommunicationMethod;
import com.umpisa.reservation.entity.Reservation;
import com.umpisa.reservation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class ReservationSchedule {
    @Autowired
    private JavaMailSender emailSender;

    @Value("${twilio.SID}")
    private String twilioSID;

    @Value("${twilio.auth}")
    private String twilioAuth;

    @Value("${twilio.phone.num}")
    private String twilioPhone;

    @Autowired
    private ReservationRepository reservationRepository;

    @Scheduled(fixedRate = 60000)
    @Async
    public void remindLatestReservations() {
        List<Reservation> reservations = Collections.synchronizedList(reservationRepository.getTobeRemindedReservations(
                Duration.of(4, ChronoUnit.HOURS).toMillis()
        ));

        for(Reservation reservation : reservations) {
            if(reservation.getPreferredCommunicationMethod().equals(CommunicationMethod.EMAIL)) {
                sendSTMPMessage(reservation);
            }
            else {
                sendSMS(reservation);
            }
        }
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
