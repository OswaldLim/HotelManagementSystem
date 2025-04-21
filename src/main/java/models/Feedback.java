package models;

import java.time.LocalDate;

public class Feedback {
    private Integer feedbackID;
    private Integer guestID;
    private String feedback;
    private String rating;
    private LocalDate created_at;

    public Feedback(Integer feedbackID, Integer guestID, String feedback, String rating, LocalDate created_at) {
        this.feedbackID = feedbackID;
        this.guestID = guestID;
        this.feedback = feedback;
        this.rating = rating;
        this.created_at = created_at;
    }

    public Integer getFeedbackID() {
        return feedbackID;
    }

    public Integer getGuestID() {
        return guestID;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getRating() {
        return rating;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }
}