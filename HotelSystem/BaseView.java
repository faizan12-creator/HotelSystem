package HotelSystem;

/**
 * Base class for all View classes in Grand Horizon.
 * Provides shared constants and utility methods.
 *
 * OOP Concept: INHERITANCE
 * AdminView and UserView extend this class to reuse
 * common hotel branding and theme constants.
 */
public abstract class BaseView {

    // ── Hotel Branding (inherited by all views) ────────────────────────
    protected static final String HOTEL_NAME    = "Grand Horizon";
    protected static final String HOTEL_TAGLINE = "Hotel & Suites";
    protected static final String HOTEL_CITY    = "Islamabad, Pakistan";
    protected static final String HOTEL_PHONE   = "+92-51-1234567";
    protected static final String HOTEL_EMAIL   = "info@grandhorizon.pk";

    // ── Theme Colors (inherited by all views) ──────────────────────────
    protected static final String BG_COLOR      = "#06060f";
    protected static final String ACCENT_COLOR  = "#9966ff";
    protected static final String TEXT_COLOR    = "#d4aaff";

    // ── Shared utility method (inherited) ──────────────────────────────
    protected static String getTitle() {
        return HOTEL_NAME + " — " + HOTEL_TAGLINE;
    }

    protected static String getFooter() {
        return "📍 " + HOTEL_CITY + "  |  📞 " + HOTEL_PHONE;
    }
}