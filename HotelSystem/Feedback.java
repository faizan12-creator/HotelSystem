package HotelSystem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * OOP Concepts:
 *   - ENCAPSULATION  : private fields, public getters
 *   - INHERITANCE    : extends BaseModel
 *   - POLYMORPHISM   : @Override getDisplayId() and getSummary()
 *   - SERIALIZATION  : implements Serializable
 */
public class Feedback extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String feedbackId, bookingId, guestName,
            comment, submittedBy, submittedAt;
    private int rating;

    public Feedback(String feedbackId, String bookingId, String guestName,
                    int rating, String comment, String submittedBy) {
        this.feedbackId  = feedbackId;
        this.bookingId   = bookingId;
        this.guestName   = guestName;
        this.rating      = rating;
        this.comment     = comment;
        this.submittedBy = submittedBy;
        this.submittedAt = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    // ── Overridden from BaseModel (Runtime Polymorphism) ──────────────
    @Override
    public String getDisplayId() {
        return feedbackId;
    }

    @Override
    public String getSummary() {
        return guestName + " | " + getStars() +
                " (" + rating + "/5) | " + comment;
    }

    // ── Getters ───────────────────────────────────────────────────────
    public String getFeedbackId()  { return feedbackId; }
    public String getBookingId()   { return bookingId; }
    public String getGuestName()   { return guestName; }
    public int    getRating()      { return rating; }
    public String getStars()       { return "⭐".repeat(rating); }
    public String getComment()     { return comment; }
    public String getSubmittedBy() { return submittedBy; }
    public String getSubmittedAt() { return submittedAt; }
}