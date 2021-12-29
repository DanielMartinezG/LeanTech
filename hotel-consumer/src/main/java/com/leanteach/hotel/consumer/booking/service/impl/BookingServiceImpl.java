package com.leanteach.hotel.consumer.booking.service.impl;

import com.amazonaws.util.CollectionUtils;
import com.leanteach.hotel.consumer.booking.dao.BookingDao;
import com.leanteach.hotel.consumer.booking.domain.dto.BookingDto;
import com.leanteach.hotel.consumer.booking.exception.BookingValidationException;
import com.leanteach.hotel.consumer.booking.mapper.BookingMapper;
import com.leanteach.hotel.consumer.booking.service.BookingService;
import com.leanteach.hotel.consumer.config.aws.domain.dto.MailDto;
import com.leanteach.hotel.consumer.entity.Booking;
import com.leanteach.hotel.consumer.entity.Person;
import com.leanteach.hotel.consumer.entity.Room;
import com.leanteach.hotel.consumer.integration.mail.MailSenderService;
import com.leanteach.hotel.consumer.room.service.RoomService;
import com.leanteach.hotel.consumer.util.Constant;
import com.leanteach.hotel.consumer.util.Messages;
import com.mysql.cj.util.StringUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Setter
@NoArgsConstructor
@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

  @Value("${cloud.aws.default-mail-to}")
  private String defaultMailTo;

  @Autowired
  private BookingDao bookingDao;

  @Autowired
  private BookingMapper bookingMapper;

  @Autowired
  private RoomService roomService;

  @Autowired
  private Validator validator;

  @Autowired
  private MailSenderService mailSenderService;

  @Autowired
  private Messages messages;

  @Override
  public void save(BookingDto bookingDto) throws BookingValidationException {
    log.info("Method BookingServiceImpl.save() bookingDto: " + bookingDto);
    Assert.notNull(bookingDto, messages.getMessages("booking.dto.is.required"));

    this.validateDto(bookingDto);

    Booking booking = this.getBooking(bookingDto);

    bookingDao.save(booking);
    this.sendSuccessEmail(booking);
    log.info("Booking successfully saved");
  }

  /**
   * Make validations and fill the booking entity
   *
   * @param bookingDto
   * @return
   * @throws BookingValidationException
   */
  private Booking getBooking(BookingDto bookingDto) throws BookingValidationException {
    log.info("Method BookingServiceImpl.getBooking() bookingDto: " + bookingDto);

    Booking booking = bookingMapper.toBooking(bookingDto);

    List<Room> availableRoomList = roomService.findByDatesAndCapacity(bookingDto);
    validateBooking(bookingDto, availableRoomList);

    fillBooking(bookingDto, booking, availableRoomList);
    return booking;
  }

  /**
   * Send success message with the booking information to the user email, in case userEmail is null it send it to default email
   *
   * @param booking
   */
  private void sendSuccessEmail(Booking booking) {
    MailDto mailDto = MailDto.builder()
        .subject(Constant.SUCCESS_SUBJECT)
        .to(booking.getReservationHolder() != null && !StringUtils.isEmptyOrWhitespaceOnly(booking.getReservationHolder().getEmail()) ? booking.getReservationHolder().getEmail() : defaultMailTo)
        .body(buildSuccessMessage(booking).toString()).build();

    mailSenderService.sendEmail(mailDto);
  }

  /**
   * Send mail to user in case there are an error
   *
   * @param bookingDto
   */
  private void sendEmailError(BookingDto bookingDto) throws BookingValidationException {
    log.info("Method BookingServiceImpl.sendEmailError() bookingDto: " + bookingDto);
    String bodyMessage = buildErrorMessage(bookingDto).toString();

    MailDto mailDto = MailDto.builder()
        .subject(Constant.ERROR_SUBJECT)
        .to(!StringUtils.isEmptyOrWhitespaceOnly(bookingDto.getEmail()) ? bookingDto.getEmail() : defaultMailTo)
        .body(bodyMessage).build();

    mailSenderService.sendEmail(mailDto);

    throw new BookingValidationException(bodyMessage);
  }

  /**
   * Build success message to send email
   *
   * @param booking
   * @return
   */
  private StringBuilder buildSuccessMessage(Booking booking) {
    StringBuilder bodyMessage = new StringBuilder(500);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.DATE_FORMAT_DD_MM_YYYY);

    bodyMessage.append(messages.getMessages("booking.success.encabezado.correo") + " \n");
    bodyMessage.append(messages.getMessages("booking.success.habitaciones.reservadas.correo") + booking.getRoom().stream().map(Room::getName).collect(Collectors.joining(", ")) + "\n");
    bodyMessage.append(messages.getMessages("booking.success.fecha.ingreso.correo") + booking.getDateFrom().format(formatter) + "\n");
    bodyMessage.append(messages.getMessages("booking.success.fecha.salida.correo") + booking.getDateTo().format(formatter) + "\n");
    bodyMessage.append(messages.getMessages("booking.success.numero.personas.correo") + booking.getNumberOfPeople() + "\n");
    bodyMessage.append("\n " + messages.getMessages("booking.success.gracias.por.preferirnos.correo"));
    return bodyMessage;
  }

  /**
   * Build error message to send email
   *
   * @param bookingDto
   * @return
   */
  private StringBuilder buildErrorMessage(BookingDto bookingDto) {
    StringBuilder bodyMessage = new StringBuilder(500);

    bodyMessage.append(messages.getMessages("booking.error.encabezado.correo") + " \n\n");
    bodyMessage.append(bookingDto.getErrorList().stream().collect(Collectors.joining("\n")));
    bodyMessage.append("\n\n" + messages.getMessages("booking.error.cambiar.parametros"));
    return bodyMessage;
  }

  /**
   * This method use bean validations in order to check every DTO field, in case there are some errors it will fill the bookingDto.errorList field
   *
   * @param bookingDto
   * @throws BookingValidationException
   */
  private void validateDto(BookingDto bookingDto) throws BookingValidationException {
    log.info("Method BookingServiceImpl.validateDto() bookingDto: " + bookingDto);

    Set<ConstraintViolation<BookingDto>> violations = validator.validate(bookingDto);

    List<String> errorList = new ArrayList<>();
    violations.stream().forEach(violation -> {
      errorList.add(violation.getMessage());
    });
    bookingDto.setErrorList(errorList);

    if (!CollectionUtils.isNullOrEmpty(bookingDto.getErrorList())) {
      this.sendEmailError(bookingDto);
    }
  }

  /**
   * Fill extra attributes to the booking entity
   *
   * @param bookingDto
   * @param booking
   * @param availableRoomList
   */
  private void fillBooking(BookingDto bookingDto, Booking booking, List<Room> availableRoomList) {
    log.info("Method BookingService.fillBooking() bookingDto: " + bookingDto + " booking: " + booking + " availableRoomList: " + availableRoomList);
    Assert.notNull(bookingDto, messages.getMessages("booking.dto.is.required"));

    associateRoomSetToBooking(bookingDto, availableRoomList, booking);
    Person reservationHolder = Person.builder()
        .name(bookingDto.getTitularReserva())
        .email(bookingDto.getEmail() !=null? bookingDto.getEmail(): null).build();

    booking.setReservationHolder(reservationHolder);
  }

  /**
   * This method associate the rooms from de DB into the booking
   *
   * @param bookingDto
   * @param availableRoomList
   * @param booking
   */
  private void associateRoomSetToBooking(BookingDto bookingDto, List<Room> availableRoomList, Booking booking) {
    log.info("Method BookingService.associateRoomSetToBooking() bookingDto: " + bookingDto + " booking: " + booking + " availableRoomList: " + availableRoomList);

    //Get the rooms that we have to associate to the booking
    Set<Room> roomSet = availableRoomList.stream()
        .limit(Long.valueOf(bookingDto.getNumeroHabitaciones()))
        .collect(Collectors.toSet());

    booking.setRoom(roomSet);
  }

  /**
   * BookingDto validations and send email in case there are issues
   *
   * @param bookingDto
   * @param availableRoomList
   * @throws BookingValidationException
   */

  private void validateBooking(BookingDto bookingDto, List<Room> availableRoomList) throws BookingValidationException {
    log.info("Method BookingServiceImpl.validateBooking() bookingDto: " + bookingDto + " availableRoomList " + availableRoomList);

    dateValidation(bookingDto);
    availabilityValidation(bookingDto, availableRoomList);

    if (!CollectionUtils.isNullOrEmpty(bookingDto.getErrorList())) {
      this.sendEmailError(bookingDto);
    }
  }

  /**
   * This method validate the disponibility to the request dates and fill the message builder
   *
   * @param bookingDto
   * @param availableRoomList
   */
  private void availabilityValidation(BookingDto bookingDto, List<Room> availableRoomList) {
    log.info("Method BookingServiceImpl.availabilityValidation() bookingDto: " + bookingDto + " availableRoomList " + availableRoomList);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constant.DATE_FORMAT_DD_MM_YYYY);
    List<String> errorList = bookingDto.getErrorList();

    if (CollectionUtils.isNullOrEmpty(availableRoomList) || availableRoomList.size() < bookingDto.getNumeroHabitaciones()) {
      errorList.add(messages.getMessages("booking.no.disponibilidad") + bookingDto.getFechaIngreso().format(formatter) + " - " + bookingDto.getFechaSalida().format(formatter));
    }

    if (!CollectionUtils.isNullOrEmpty(errorList)) {
      bookingDto.setErrorList(errorList);
    }
  }

  /**
   * Date validations to the request and fill it into the error message list
   *
   * @param bookingDto
   */
  private void dateValidation(BookingDto bookingDto) {
    log.info("Method BookingServiceImpl.dateValidation() bookingDto: " + bookingDto);

    Assert.notNull(bookingDto, messages.getMessages("booking.dto.is.required"));
    Assert.notNull(bookingDto.getFechaIngreso(), messages.getMessages("fecha.ingreso.is.required"));
    Assert.notNull(bookingDto.getFechaSalida(), messages.getMessages("fecha.salida.is.required"));
    Assert.notNull(bookingDto.getTotalDias(), messages.getMessages("total.dias.is.required"));

    List<String> errorList = bookingDto.getErrorList();

    if (bookingDto.getFechaSalida().isBefore(bookingDto.getFechaIngreso())) {
      errorList.add(messages.getMessages("fecha.salida.menor.a.fecha.entrada"));
    }

    if (bookingDto.getFechaIngreso().isBefore(LocalDate.now())) {
      errorList.add(messages.getMessages("booking.para.fecha.pasada"));
    }

    if (!Long.valueOf(bookingDto.getTotalDias()).equals(bookingDto.getFechaIngreso().until(bookingDto.getFechaSalida(), ChronoUnit.DAYS))) {
      errorList.add(messages.getMessages("dias.no.concuerdan"));
    }

    if (!CollectionUtils.isNullOrEmpty(errorList)) {
      bookingDto.setErrorList(errorList);
    }
  }
}
