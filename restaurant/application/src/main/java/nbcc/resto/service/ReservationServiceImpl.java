package nbcc.resto.service;

import nbcc.common.exception.ConcurrencyException;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.email.domain.EmailRequest;
import nbcc.email.service.EmailService;
import nbcc.resto.dto.*;
import nbcc.resto.repository.ReservationRepository;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.repository.TableAssignmentRepository;
import nbcc.resto.validation.ReservationValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    private final ReservationValidationService validationService;
    private final ReservationRepository reservationRepository;
    private final SeatingRepository seatingRepository;
    private final TableAssignmentService assignmentService;
    private final EmailService emailService;

    public ReservationServiceImpl(ReservationValidationService validationService, ReservationRepository reservationRepository, SeatingRepository seatingRepository, TableAssignmentService assignmentService, EmailService emailService) {
        this.validationService = validationService;
        this.reservationRepository = reservationRepository;
        this.seatingRepository = seatingRepository;
        this.assignmentService = assignmentService;
        this.emailService = emailService;
    }

    @Override
    public ValidatedResult<Reservation> create(Reservation reservation) {
        try {
            var errors = validationService.validate(reservation);

            if (errors.isEmpty()) {
                reservation.setStatus(ReservationStatus.PENDING);
                reservation.setReservationId(UUID.randomUUID().toString());

                var result = reservationRepository.create(reservation);

                if (result.getEmail() != null) {
                    sendEmail(reservation, false);
                }

                return ValidationResults.success(result);
            } else {
                logger.debug("Validation errors for reservation create {}: {}", reservation, errors);
                return ValidationResults.invalid(reservation, errors);
            }
        } catch (Exception e) {
            logger.error("Error while creating reservation {}", reservation, e);
            return ValidationResults.error(reservation);
        }
    }

    @Override
    public ValidatedResult<Collection<Reservation>> getAll(Long eventId) {
        try {
            return ValidationResults.success(reservationRepository.getAll(eventId));
        } catch (Exception e) {
            logger.error("Error retrieving all reservations for event with id {}",eventId, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<Reservation>> getAll() {
        try {
            return ValidationResults.success(reservationRepository.getAll());
        } catch (Exception e) {
            logger.error("Error retrieving all reservations.", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Reservation> get(Long id) {
        try {
            return ValidationResults.success(reservationRepository.get(id));
        } catch (Exception e) {
            logger.error("Error retrieving reservation with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<Reservation>> filter(Long eventId, ReservationStatus status) {
        try {
            var result = reservationRepository.filter(eventId, status);
            return ValidationResults.success(result);
        } catch (Exception e) {
            logger.error("Error filtering all reservations for event wth id {}", eventId, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Reservation> getByUUID(String reservationId) {
        try {
            var result = reservationRepository.getByUUID(reservationId);

            if (result.isPresent()) {
                return ValidationResults.success(result);
            }

            return ValidationResults.invalid(new Reservation(), new ValidationError("Reservation not found."));
        } catch (Exception e) {
            logger.error("Error retrieving reservation with UUID: {}", reservationId, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<Reservation>> getApproved(Long seatingId) {
        try {
            return ValidationResults.success(reservationRepository.getApproved(seatingId));
        } catch (Exception e) {
            logger.error("Error retrieving all approved reservations for seating with id {}", seatingId, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<DiningTable>> getAvailableTables(Long seatingId) {
        try {

            var optionalSeating = seatingRepository.get(seatingId);

            if (optionalSeating.isPresent()) {
                Seating seating = optionalSeating.get();

                var tables = assignmentService.getAll(seating.getId()).getValue();
                var approvedReservations = reservationRepository.getApproved(seating.getId());

                var availableTables = new ArrayList<DiningTable>();
                var usedTables = new ArrayList<Long>();

                for (Reservation r: approvedReservations) {
                    if (r.getAssignedTable() != null)
                        usedTables.add(r.getAssignedTable().getId());
                }

                for(DiningTable table: tables) {
                    if (!usedTables.contains(table.getId())) {
                        availableTables.add(table);
                    }
                }

                return ValidationResults.success(availableTables);

            }

            return ValidationResults.success(new ArrayList<>());
        } catch (Exception e) {
            logger.error("Error retrieving all approved reservations for seating with id {}", seatingId, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Reservation> update(Reservation reservation) {
        try {
            var errors = validationService.validate(reservation);

            if (errors.isEmpty()) {
                try {
                    var result = reservationRepository.update(reservation);
                    if (result.getEmail() != null) {
                        sendEmail(reservation, true);
                    }
                    return ValidationResults.success(result);
                } catch(ConcurrencyException e) {
                    errors.add(new ValidationError("This reservation was modified since it was displayed, please refresh and try again"));
                }
            }
            logger.debug("Validation errors found for Reservation update {}: {}", reservation, errors);
            return ValidationResults.invalid(reservation, errors);

        } catch (Exception e) {
            logger.error("Error updating reservation {}",reservation, e);
            return ValidationResults.error(reservation);
        }
    }

    private void sendEmail(Reservation reservation, boolean isUpdate) {
        try {

            if (reservation == null) { return; }

            String subject = "";
            String body = "";

            UrbanPlatterEvent platterEvent = reservation.getSeating().getPlatterEvent();
            String guestFullName = reservation.getFirstName() + " " + reservation.getLastName();
            LocalDateTime startDateTime = reservation.getSeating().getStartDate();
            Integer groupSize = reservation.getGroupSize();
            String reservationId = reservation.getReservationId();
            String requestStatus = reservation.getStatus().toString();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");
            String formattedStartDate = startDateTime.format(formatter);

            if (isUpdate && reservation.getStatus().equals(ReservationStatus.APPROVED)) {

                subject = "Update on Your Reservation Request";
                body = """
                        Hello %s,
                        
                        Your reservation has been approved! 🎉
                        
                        Details
                        • Event: %s
                        • Seating Date and Time: %s
                        • Group Size: %d
                        • Status: %s
                        
                        You can view more details about your reservation here: 
                        http://localhost:8080/reservation/booking/%s"
                        
                        We look forward to seeing you! 
                        
                        Kind regards,
                        Urban Platter Restaurant
                        """.formatted(guestFullName, platterEvent.getName(), formattedStartDate, groupSize,requestStatus, reservationId);
            } else if (isUpdate && reservation.getStatus().equals(ReservationStatus.DENIED)) {

                subject = "Update on Your Reservation Request";
                body = """
                        Hello %s,
                        
                        Thank you for your reservation request.
                        
                        Unfortunately, we are unable to accommodate your booking for the selected date and time.
                        This may be due to limited availability. 
                        
                        We sincerely apologize for any inconvenience. 
                        We would happy to assist you in finding an alternative date or time. Please feel free to contact us. 
                        
                        Reservation Request Details
                        • Event: %s
                        • Seating Date and Time: %s
                        • Group Size: %d 
                        • Status: %s
                        
                        Kind regards,
                        Urban Platter Restaurant
                        """.formatted(guestFullName, platterEvent.getName(), formattedStartDate, groupSize, requestStatus);
            } else {
                subject = "Reservation Received";
                body = """
                        Hello %s,
                        
                        We have received you reservation request successfully. 
                        
                        Here are your details:
                        • Event: %s
                        • Seating Date and Time: %s
                        • Group Size: %d 
                        • Status: %s
                        
                        Your request is currently being reviewed. We will notify you once it has been approved or if any changes are needed.
                        
                        You can view more details about your reservation here: 
                        http://localhost:8080/reservation/booking/%s"
                        
                        Thank you for choosing us! 
                        
                        Kind regards,
                        Urban Platter Restaurant
                        
                        """.formatted(guestFullName, platterEvent.getName(), formattedStartDate, groupSize, requestStatus, reservationId);
            }

            var emailRequest = new EmailRequest()
                    .setTo(reservation.getEmail())
                    .setSubject(subject)
                    .setBody(body);

            logger.info("Sending email to {}", reservation.getEmail());

            if (emailService.sendEmail(emailRequest)) {
                logger.info("Email sent to {}", reservation.getEmail());
            } else {
                logger.warn("Could not send email to {}", reservation.getEmail());
            }
        } catch (Exception e) {
            logger.error("Error sending email to {}", reservation.getEmail(), e);
        }
    }
}
