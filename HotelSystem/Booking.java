package HotelSystem;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * OOP Concepts:
 *   - ENCAPSULATION  : all fields private, accessed via getters/setters
 *   - INHERITANCE    : extends BaseModel
 *   - POLYMORPHISM   : @Override getDisplayId() and getSummary()
 *   - SERIALIZATION  : implements Serializable for file persistence
 */
public class Booking extends BaseModel implements Serializable {

    private static final long serialVersionUID = 2L;

    private String bookingId, guestName, roomType, checkIn, checkOut,
            status, payment, paymentMethod, phone, email, bookedBy, bookedAt;
    private double totalPrice;
    private int nights;

    public Booking(String id, String name, String room, String ci, String co,
                   double ppn, String ph, String em, String by) {
        this.bookingId     = id;
        this.guestName     = name;
        this.roomType      = room;
        this.checkIn       = ci;
        this.checkOut      = co;
        this.phone         = ph;
        this.email         = em;
        this.bookedBy      = by;
        this.status        = "Confirmed";
        this.payment       = "Pending";
        this.paymentMethod = "—";
        this.bookedAt      = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        try {
            this.nights = (int) ChronoUnit.DAYS.between(
                    LocalDate.parse(ci), LocalDate.parse(co));
        } catch (Exception e) {
            this.nights = 1;
        }
        this.totalPrice = ppn * Math.max(1, nights);
    }

    // ── Overridden from BaseModel (Runtime Polymorphism) ──────────────
    @Override
    public String getDisplayId() {
        return bookingId;
    }

    @Override
    public String getSummary() {
        return guestName + " | " + roomType +
                " | " + checkIn + " → " + checkOut +
                " | Rs " + String.format("%,.0f", totalPrice) +
                " | " + status;
    }

    // ── Getters ───────────────────────────────────────────────────────
    public String getBookingId()     { return bookingId; }
    public String getGuestName()     { return guestName; }
    public String getRoomType()      { return roomType; }
    public String getCheckIn()       { return checkIn; }
    public String getCheckOut()      { return checkOut; }
    public String getStatus()        { return status; }
    public String getPayment()       { return payment; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPhone()         { return phone; }
    public String getEmail()         { return email; }
    public String getBookedBy()      { return bookedBy; }
    public String getBookedAt()      { return bookedAt; }
    public double getTotalPrice()    { return totalPrice; }
    public int    getNights()        { return nights; }

    // ── Setters ───────────────────────────────────────────────────────
    public void setStatus(String s)        { this.status = s; }
    public void setPayment(String p)       { this.payment = p; }
    public void setPaymentMethod(String m) { this.paymentMethod = m; }
}