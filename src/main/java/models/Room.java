package models;

import javafx.scene.image.Image;

public class Room {
    private Integer roomIdentificationNumber;
    private Integer roomCapacity;
    private Double roomPricing;
    private String roomType;
    private Image image;
    private String roomStatus;

    public Room(Integer roomIdentificationNumber, Integer roomCapacity, Double roomPricing, String roomType, Image image, String roomStatus) {
        this.roomIdentificationNumber = roomIdentificationNumber;
        this.roomCapacity = roomCapacity;
        this.roomPricing = roomPricing;
        this.roomType = roomType;
        this.image = image;
        this.roomStatus = roomStatus;
    }

    public Integer getRoomIdentificationNumber() {
        return roomIdentificationNumber;
    }

    public Integer getRoomCapacity() {
        return roomCapacity;
    }

    public Double getRoomPricing() {
        return roomPricing;
    }

    public String getRoomType() {
        return roomType;
    }

    public Image getImage() {
        return image;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomIdentificationNumber(Integer roomIdentificationNumber) {
        this.roomIdentificationNumber = roomIdentificationNumber;
    }

    public void setRoomCapacity(Integer roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public void setRoomPricing(Double roomPricing) {
        this.roomPricing = roomPricing;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setPicturePath(Image image) {
        this.image = image;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }
}
