
package HotelSystem;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RoomServiceOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId, bookingId, guestName, items, status, orderedBy, orderedAt;
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

    public String getOrderId()    { return orderId; }
    public String getBookingId()  { return bookingId; }
    public String getGuestName()  { return guestName; }
    public String getItems()      { return items; }
    public double getTotalCost()  { return totalCost; }
    public String getStatus()     { return status; }
    public String getOrderedBy()  { return orderedBy; }
    public String getOrderedAt()  { return orderedAt; }
    public void   setStatus(String s) { this.status = s; }
}