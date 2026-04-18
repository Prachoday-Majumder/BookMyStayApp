import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * UseCase11ConcurrentBookingSimulation - Simulates concurrent room booking using threads.
 * Demonstrates race conditions, synchronized critical sections, and thread-safe inventory management.
 *
 * @author Prachoday
 * @version 11.0
 */

/** Thread-safe centralized inventory using synchronized methods. */
class ThreadSafeInventory {
    private HashMap<String, Integer> inventory;

    public ThreadSafeInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 3);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room",  1);
    }

    /**
     * Attempts to allocate a room of the given type.
     * Synchronized to prevent race conditions on shared inventory state.
     *
     * @param roomType  the type of room to allocate
     * @param guestName the name of the requesting guest
     * @return true if allocation succeeded, false otherwise
     */
    public synchronized boolean allocate(String roomType, String guestName) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            System.out.println("  [" + Thread.currentThread().getName() + "] CONFIRMED: "
                    + guestName + " -> " + roomType + " | Remaining: " + (available - 1));
            return true;
        } else {
            System.out.println("  [" + Thread.currentThread().getName() + "] FAILED   : "
                    + guestName + " -> " + roomType + " | No rooms available");
            return false;
        }
    }

    /** Displays final inventory state after all threads complete. */
    public synchronized void displayInventory() {
        System.out.println("\n--- Final Inventory State ---");
        for (HashMap.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.printf("  %-15s : %d remaining%n", e.getKey(), e.getValue());
        }
    }
}

/** Represents a booking request task run by a guest thread. */
class BookingTask implements Runnable {
    private String guestName;
    private String roomType;
    private ThreadSafeInventory inventory;

    public BookingTask(String guestName, String roomType, ThreadSafeInventory inventory) {
        this.guestName = guestName;
        this.roomType  = roomType;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        inventory.allocate(roomType, guestName);
    }
}

/** Application entry point for Use Case 11. */
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("============================================");
        System.out.println("   Hotel Booking System v11.0              ");
        System.out.println("   Use Case 11: Concurrent Booking Sim      ");
        System.out.println("============================================");

        ThreadSafeInventory inventory = new ThreadSafeInventory();

        // Define concurrent booking requests (more than available to test thread safety)
        String[][] requests = {
            {"Alice",   "Single Room"},
            {"Bob",     "Single Room"},
            {"Charlie", "Single Room"},
            {"Diana",   "Single Room"},   // Should fail – only 3 available
            {"Ethan",   "Double Room"},
            {"Fiona",   "Double Room"},
            {"George",  "Double Room"},   // Should fail – only 2 available
            {"Hana",    "Suite Room"},
            {"Ivan",    "Suite Room"},    // Should fail – only 1 available
        };

        Thread[] threads = new Thread[requests.length];

        System.out.println("\n--- Launching " + requests.length + " Concurrent Booking Threads ---\n");

        for (int i = 0; i < requests.length; i++) {
            BookingTask task = new BookingTask(requests[i][0], requests[i][1], inventory);
            threads[i] = new Thread(task, "Thread-" + (i + 1));
        }

        // Start all threads concurrently
        for (Thread t : threads) {
            t.start();
        }

        // Wait for all threads to finish
        for (Thread t : threads) {
            t.join();
        }

        inventory.displayInventory();

        System.out.println("\n============================================");
        System.out.println("  Concurrent simulation complete.");
        System.out.println("  No double-booking occurred.");
        System.out.println("============================================");
    }
}
