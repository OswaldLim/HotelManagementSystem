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
}
