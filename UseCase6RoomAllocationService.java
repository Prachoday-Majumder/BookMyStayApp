import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * UseCase6RoomAllocationService - Confirms bookings and assigns unique room IDs.
 * Uses a Set to prevent double-booking and updates inventory atomically.
 *
 * @author Prachoday
 * @version 6.0
 */

/** Represents a guest's booking reservation request. */
class BookingReservation {
    private String guestName;
    private String roomType;
    private int numberOfNights;

    public BookingReservation(String guestName, String roomType, int numberOfNights) {
        this.guestName      = guestName;
        this.roomType       = roomType;
        this.numberOfNights = numberOfNights;
    }

    public String getGuestName()   { return guestName; }
    public String getRoomType()    { return roomType; }
    public int getNumberOfNights() { return numberOfNights; }

    @Override
    public String toString() {
        return "Guest: " + guestName + " | Room: " + roomType + " | Nights: " + numberOfNights;
    }
}

/** Manages room inventory counts. */
class InventoryManager {
    private HashMap<String, Integer> inventory;

    public InventoryManager() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 3);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room",  1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrement(String roomType) {
        int current = inventory.getOrDefault(roomType, 0);
        if (current > 0) inventory.put(roomType, current - 1);
    }

    public void displayInventory() {
        System.out.println("\n--- Inventory State ---");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.printf("  %-15s : %d%n", e.getKey(), e.getValue());
        }
        System.out.println();
    }
}

/** Processes booking requests, assigns unique room IDs, and prevents double-booking. */
class RoomAllocationService {
    private InventoryManager inventoryManager;
    private Set<String> allocatedRoomIds;
    private HashMap<String, Set<String>> roomTypeAllocations;
    private int roomIdCounter;

    public RoomAllocationService(InventoryManager inventoryManager) {
        this.inventoryManager    = inventoryManager;
        this.allocatedRoomIds    = new HashSet<>();
        this.roomTypeAllocations = new HashMap<>();
        this.roomIdCounter       = 1000;
    }

    /**
     * Processes a booking request from the queue.
     * Assigns a unique room ID, updates inventory, and records the allocation.
     *
     * @param reservation the booking request to process
     */
    public void processRequest(BookingReservation reservation) {
        String roomType = reservation.getRoomType();

        if (inventoryManager.getAvailability(roomType) <= 0) {
            System.out.println("  [FAILED]  No rooms available for " + reservation.getGuestName()
                    + " (" + roomType + ")");
            return;
        }

        // Generate a unique room ID
        String roomId = roomType.substring(0, 1).toUpperCase() + "-" + (++roomIdCounter);

        // Prevent reuse of room IDs (Set enforces uniqueness)
        while (allocatedRoomIds.contains(roomId)) {
            roomId = roomType.substring(0, 1).toUpperCase() + "-" + (++roomIdCounter);
        }

        // Record allocation
        allocatedRoomIds.add(roomId);
        roomTypeAllocations.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);

        // Decrement inventory atomically
        inventoryManager.decrement(roomType);

        System.out.println("  [CONFIRMED] " + reservation.getGuestName()
                + " -> Room ID: " + roomId + " (" + roomType + ")");
    }

    /** Displays all room allocations grouped by type. */
    public void displayAllocations() {
        System.out.println("\n--- Room Allocations by Type ---");
        for (Map.Entry<String, Set<String>> e : roomTypeAllocations.entrySet()) {
            System.out.println("  " + e.getKey() + " : " + e.getValue());
        }
        System.out.println();
    }
}

/** Application entry point for Use Case 6. */
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Hotel Booking System v6.0               ");
        System.out.println("   Use Case 6: Reservation Confirmation     ");
        System.out.println("============================================");

        InventoryManager inventory = new InventoryManager();
        inventory.displayInventory();

        // Build request queue
        Queue<BookingReservation> queue = new LinkedList<>();
        queue.offer(new BookingReservation("Alice",   "Single Room", 3));
        queue.offer(new BookingReservation("Bob",     "Double Room", 2));
        queue.offer(new BookingReservation("Charlie", "Suite Room",  5));
        queue.offer(new BookingReservation("Diana",   "Single Room", 1));
        queue.offer(new BookingReservation("Ethan",   "Single Room", 2)); // will exceed availability
        queue.offer(new BookingReservation("Fiona",   "Double Room", 3)); // will fail
        queue.offer(new BookingReservation("George",  "Single Room", 1)); // will fail

        RoomAllocationService allocationService = new RoomAllocationService(inventory);

        System.out.println("--- Processing Booking Queue ---\n");
        while (!queue.isEmpty()) {
            BookingReservation request = queue.poll();
            allocationService.processRequest(request);
        }

        allocationService.displayAllocations();
        inventory.displayInventory();

        System.out.println("============================================");
        System.out.println("  Room allocation complete.");
        System.out.println("============================================");
    }
}
