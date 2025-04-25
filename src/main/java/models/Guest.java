package models;

import javafx.scene.image.Image;

public class Guest {
    private Integer guestID;
    private String guestLastName;
    private String ICNum;
    private String email;
    private String phoneNumber;
    private Image profilePic;

    public Guest(Integer guestID, String guestLastName, String ICNum, String email, String phoneNumber, Image profilePic) {
        this.guestID = guestID;
        this.guestLastName = guestLastName;
        this.ICNum = ICNum;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePic = profilePic;
    }

    public Image getProfilePic(){
        return profilePic;
    }

    public Integer getGuestID() {
        return guestID;
    }

    public String getGuestLastName() {
        return guestLastName;
    }

    public String getICNum() {
        return ICNum;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setGuestID(Integer guestID) {
        this.guestID = guestID;
    }

    public void setGuestLastName(String guestLastName) {
        this.guestLastName = guestLastName;
    }

    public void setICNum(String ICNum) {
        this.ICNum = ICNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfilePic(Image profilePic) {
        this.profilePic = profilePic;
    }
}
