import java.util.ArrayList;
import java.util.List;

/**
 * UseCase8BookingHistoryReport - Maintains confirmed booking history and generates reports.
 * Uses an ordered List for chronological storage and a dedicated reporting service.
 *
 * @author Prachoday
 * @version 8.0
 */

/** Represents a confirmed hotel reservation. */
class ConfirmedBooking {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private int numberOfNights;
    private double pricePerNight;

    public ConfirmedBooking(String reservationId, String guestName, String roomType,
                            String roomId, int numberOfNights, double pricePerNight) {
        this.reservationId  = reservationId;
        this.guestName      = guestName;
        this.roomType       = roomType;
        this.roomId         = roomId;
        this.numberOfNights = numberOfNights;
        this.pricePerNight  = pricePerNight;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName()     { return guestName; }
    public String getRoomType()      { return roomType; }
    public String getRoomId()        { return roomId; }
    public int getNumberOfNights()   { return numberOfNights; }
    public double getTotalCost()     { return numberOfNights * pricePerNight; }

    @Override
    public String toString() {
        return "ID: " + reservationId + " | Guest: " + guestName
                + " | " + roomType + " (" + roomId + ")"
                + " | Nights: " + numberOfNights
                + " | Total: $" + String.format("%.2f", getTotalCost());
    }
}

/** Stores confirmed bookings in insertion (chronological) order. */
class BookingHistory {
    private List<ConfirmedBooking> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    /**
     * Adds a confirmed booking to the history.
     *
     * @param booking the confirmed booking to record
     */
    public void addBooking(ConfirmedBooking booking) {
        history.add(booking);
        System.out.println("  [Recorded] " + booking.getReservationId() + " for " + booking.getGuestName());
    }

    public List<ConfirmedBooking> getHistory() {
        return history;
    }
}

/** Generates reports from booking history without modifying stored data. */
class BookingReportService {
    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    /** Displays all bookings in chronological order. */
    public void printFullReport() {
        System.out.println("\n--- Full Booking History Report ---\n");
        List<ConfirmedBooking> history = bookingHistory.getHistory();
        if (history.isEmpty()) {
            System.out.println("  No bookings recorded.");
            return;
        }
        int index = 1;
        for (ConfirmedBooking b : history) {
            System.out.println("  " + index + ". " + b);
            index++;
        }
    }

    /** Prints a summary of total bookings and revenue. */
    public void printSummary() {
        System.out.println("\n--- Booking Summary ---\n");
        List<ConfirmedBooking> history = bookingHistory.getHistory();
        int totalBookings = history.size();
        double totalRevenue = 0.0;

        for (ConfirmedBooking b : history) {
            totalRevenue += b.getTotalCost();
        }

        System.out.println("  Total Bookings  : " + totalBookings);
        System.out.printf ("  Total Revenue   : $%.2f%n", totalRevenue);
        System.out.println();
    }
}

/** Application entry point for Use Case 8. */
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Hotel Booking System v8.0               ");
        System.out.println("   Use Case 8: Booking History & Reporting  ");
        System.out.println("============================================");

        BookingHistory bookingHistory = new BookingHistory();

        System.out.println("\n--- Confirming Bookings ---\n");
        bookingHistory.addBooking(new ConfirmedBooking("S-1001", "Alice",   "Single Room", "S-1001", 3, 100.0));
        bookingHistory.addBooking(new ConfirmedBooking("D-1002", "Bob",     "Double Room", "D-1002", 2, 150.0));
        bookingHistory.addBooking(new ConfirmedBooking("X-1003", "Charlie", "Suite Room",  "X-1003", 5, 300.0));
        bookingHistory.addBooking(new ConfirmedBooking("S-1004", "Diana",   "Single Room", "S-1004", 1, 100.0));

        BookingReportService reportService = new BookingReportService(bookingHistory);
        reportService.printFullReport();
        reportService.printSummary();

        System.out.println("============================================");
        System.out.println("  Booking history report complete.");
        System.out.println("============================================");
    }
}
