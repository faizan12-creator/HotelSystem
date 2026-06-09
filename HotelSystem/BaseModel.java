package HotelSystem;

public abstract class BaseModel {

    public abstract String getDisplayId();
    public abstract String getSummary();

    // Shared method — sab inherit karte hain
    public String getFormattedEntry() {
        return "[" + getDisplayId() + "] " + getSummary();
    }
}