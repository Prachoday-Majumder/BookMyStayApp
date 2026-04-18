import java.util.LinkedList;
import java.util.Queue;

/**
 * UseCase5BookingRequestQueue - Handles multiple booking requests fairly using FIFO queue.
 * Demonstrates the Queue data structure for first-come-first-served request management.
 *
 * @author Prachoday
 * @version 5.0
 */

/** Represents a guest's intent to book a specific room type. */
class Reservation {
    private String guestName;
    private String roomType;
    private int numberOfNights;

    public Reservation(String guestName, String roomType, int numberOfNights) {
        this.guestName      = guestName;
        this.roomType       = roomType;
        this.numberOfNights = numberOfNights;
    }

    public String getGuestName()      { return guestName; }
    public String getRoomType()       { return roomType; }
    public int getNumberOfNights()    { return numberOfNights; }

    @Override
    public String toString() {
        return "Guest: " + guestName + " | Room: " + roomType + " | Nights: " + numberOfNights;
    }
}

/** Manages and orders incoming booking requests using a FIFO queue. */
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    /**
     * Adds a booking request to the queue.
     *
     * @param reservation the guest's reservation request
     */
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("  [Queued] " + reservation);
    }

    /** Displays all queued requests in arrival order without dequeuing them. */
    public void displayQueue() {
        System.out.println("\n--- Booking Request Queue (FIFO Order) ---\n");
        if (requestQueue.isEmpty()) {
            System.out.println("  No pending booking requests.");
            return;
        }
        int position = 1;
        for (Reservation r : requestQueue) {
            System.out.println("  Position " + position + ": " + r);
            position++;
        }
        System.out.println();
    }

    public int getQueueSize() {
        return requestQueue.size();
    }

    public Queue<Reservation> getQueue() {
        return requestQueue;
    }
}

/** Application entry point for Use Case 5. */
public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Hotel Booking System v5.0               ");
        System.out.println("   Use Case 5: Booking Request Queue        ");
        System.out.println("============================================");

        BookingRequestQueue requestQueue = new BookingRequestQueue();

        System.out.println("\n--- Accepting Booking Requests ---\n");
        requestQueue.addRequest(new Reservation("Alice",   "Single Room", 3));
        requestQueue.addRequest(new Reservation("Bob",     "Double Room", 2));
        requestQueue.addRequest(new Reservation("Charlie", "Suite Room",  5));
        requestQueue.addRequest(new Reservation("Diana",   "Single Room", 1));
        requestQueue.addRequest(new Reservation("Ethan",   "Double Room", 4));

        requestQueue.displayQueue();

        System.out.println("  Total Queued Requests : " + requestQueue.getQueueSize());
        System.out.println("  [Note] No inventory changes made at this stage.");

        System.out.println("\n============================================");
        System.out.println("  Booking request intake complete.");
        System.out.println("============================================");
    }
}
