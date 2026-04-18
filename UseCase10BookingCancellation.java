import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * UseCase10BookingCancellation - Enables safe cancellation with inventory rollback using a Stack.
 * Demonstrates LIFO rollback behavior and controlled state reversal.
 *
 * @author Prachoday
 * @version 10.0
 */

/** Represents a confirmed reservation that can be cancelled. */
class CancellableReservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean cancelled;

    public CancellableReservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName     = guestName;
        this.roomType      = roomType;
        this.roomId        = roomId;
        this.cancelled     = false;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName()     { return guestName; }
    public String getRoomType()      { return roomType; }
    public String getRoomId()        { return roomId; }
    public boolean isCancelled()     { return cancelled; }
    public void markCancelled()      { this.cancelled = true; }

    @Override
    public String toString() {
        return "ID: " + reservationId + " | Guest: " + guestName
                + " | " + roomType + " (" + roomId + ")"
                + " | Status: " + (cancelled ? "CANCELLED" : "CONFIRMED");
    }
}

/** Manages cancellations and rolls back inventory using a Stack. */
class CancellationService {
    private HashMap<String, CancellableReservation> reservations;
    private HashMap<String, Integer> inventory;
    private Stack<String> releasedRoomIds;   // LIFO rollback tracker

    public CancellationService() {
        reservations    = new HashMap<>();
        releasedRoomIds = new Stack<>();

        // Seed inventory
        inventory = new HashMap<>();
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room",  1);
    }

    /** Registers a confirmed reservation. */
    public void registerReservation(CancellableReservation res) {
        reservations.put(res.getReservationId(), res);
        System.out.println("  [Registered] " + res);
    }

    /**
     * Cancels a booking, releases the room ID, and restores inventory.
     *
     * @param reservationId the ID of the reservation to cancel
     */
    public void cancelBooking(String reservationId) {
        if (!reservations.containsKey(reservationId)) {
            System.out.println("  [FAILED]  Reservation not found: " + reservationId);
            return;
        }

        CancellableReservation res = reservations.get(reservationId);

        if (res.isCancelled()) {
            System.out.println("  [FAILED]  Already cancelled: " + reservationId);
            return;
        }

        // Mark as cancelled
        res.markCancelled();

        // Push released room ID onto the stack (LIFO rollback)
        releasedRoomIds.push(res.getRoomId());

        // Restore inventory
        String roomType = res.getRoomType();
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);

        System.out.println("  [CANCELLED] Reservation " + reservationId
                + " | Room ID released: " + res.getRoomId()
                + " | " + roomType + " inventory restored.");
    }

    /** Displays current inventory after rollback operations. */
    public void displayInventory() {
        System.out.println("\n--- Inventory After Cancellations ---");
        for (HashMap.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.printf("  %-15s : %d%n", e.getKey(), e.getValue());
        }
    }

    /** Displays the stack of released room IDs (most recent on top). */
    public void displayReleasedRooms() {
        System.out.println("\n--- Released Room IDs (LIFO Stack) ---");
        System.out.println("  " + releasedRoomIds + " (top = most recently released)");
    }

    /** Displays the booking history (including cancellation status). */
    public void displayAllReservations() {
        System.out.println("\n--- All Reservations ---");
        for (CancellableReservation r : reservations.values()) {
            System.out.println("  " + r);
        }
    }
}

/** Application entry point for Use Case 10. */
public class UseCase10BookingCancellation {

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Hotel Booking System v10.0              ");
        System.out.println("   Use Case 10: Booking Cancellation        ");
        System.out.println("============================================");

        CancellationService service = new CancellationService();

        System.out.println("\n--- Registering Confirmed Bookings ---\n");
        service.registerReservation(new CancellableReservation("S-1001", "Alice",   "Single Room", "S-1001"));
        service.registerReservation(new CancellableReservation("D-1002", "Bob",     "Double Room", "D-1002"));
        service.registerReservation(new CancellableReservation("X-1003", "Charlie", "Suite Room",  "X-1003"));

        System.out.println("\n--- Processing Cancellation Requests ---\n");
        service.cancelBooking("D-1002");                // Valid cancellation
        service.cancelBooking("D-1002");                // Duplicate – should fail
        service.cancelBooking("Z-9999");                // Non-existent – should fail
        service.cancelBooking("X-1003");                // Valid cancellation

        service.displayReleasedRooms();
        service.displayInventory();
        service.displayAllReservations();

        System.out.println("\n============================================");
        System.out.println("  Cancellation and rollback complete.");
        System.out.println("============================================");
    }
}
