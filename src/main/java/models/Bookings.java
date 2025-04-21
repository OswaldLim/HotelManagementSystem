package models;

import java.time.LocalDate;

public class Bookings {
    private Integer bookingID;
    private Integer guestID;
    private Integer roomID;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Double totalAmount;
    private String paymentType;
    private LocalDate bookingDate;
    private String status;

    public Bookings(Integer bookingID, Integer guestID, Integer roomID, LocalDate checkInDate, LocalDate checkOutDate, Double totalAmount, String paymentType, LocalDate bookingDate, String status) {
        this.bookingID = bookingID;
        this.guestID = guestID;
        this.roomID = roomID;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.paymentType = paymentType;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public Integer getBookingID() {
        return bookingID;
    }

    public Integer getGuestID() {
        return guestID;
    }

    public Integer getRoomID() {
        return roomID;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getStatus() {
        return status;
    }

    public void setBookingID(Integer bookingID) {
        this.bookingID = bookingID;
    }

    public void setGuestID(Integer guestID) {
        this.guestID = guestID;
    }

    public void setRoomID(Integer roomID) {
        this.roomID = roomID;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}