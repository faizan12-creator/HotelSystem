package HotelSystem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * OOP Concepts:
 *   - ENCAPSULATION  : private fields, public getters/setters
 *   - INHERITANCE    : extends BaseModel
 *   - POLYMORPHISM   : @Override getDisplayId() and getSummary()
 *   - SERIALIZATION  : implements Serializable
 */
public class RoomServiceOrder extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderId, bookingId, guestName,
            items, status, orderedBy, orderedAt;
    private double totalCost;

    public RoomServiceOrder(String orderId, String bookingId, String guestName,
                            String items, double totalCost, String orderedBy) {
        this.orderId   = orderId;
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.items     = items;
        this.totalCost = totalCost;
        this.orderedBy = orderedBy;
        this.status    = "Pending";
        this.orderedAt = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    // ── Overridden from BaseModel (Runtime Polymorphism) ──────────────
    @Override
    public String getDisplayId() {
        return orderId;
    }

    @Override
    public String getSummary() {
        return guestName + " | " + items +
                " | Rs " + String.format("%,.0f", totalCost) +
                " | " + status;
    }

    // ── Getters ───────────────────────────────────────────────────────
    public String getOrderId()    { return orderId; }
    public String getBookingId()  { return bookingId; }
    public String getGuestName()  { return guestName; }
    public String getItems()      { return items; }
    public double getTotalCost()  { return totalCost; }
    public String getStatus()     { return status; }
    public String getOrderedBy()  { return orderedBy; }
    public String getOrderedAt()  { return orderedAt; }

    // ── Setter ────────────────────────────────────────────────────────
    public void setStatus(String s) { this.status = s; }
}