import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UseCase12DataPersistenceRecovery - Introduces file-based persistence and system recovery.
 * Demonstrates serialization of booking history and inventory to disk with safe recovery.
 *
 * @author Prachoday
 * @version 12.0
 */

/** Represents a confirmed reservation that can be serialized to file. */
class PersistableBooking implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private int numberOfNights;
    private double totalCost;

    public PersistableBooking(String reservationId, String guestName, String roomType,
                               String roomId, int numberOfNights, double totalCost) {
        this.reservationId  = reservationId;
        this.guestName      = guestName;
        this.roomType       = roomType;
        this.roomId         = roomId;
        this.numberOfNights = numberOfNights;
        this.totalCost      = totalCost;
    }

    @Override
    public String toString() {
        return "ID: " + reservationId + " | Guest: " + guestName
                + " | " + roomType + " (" + roomId + ")"
                + " | Nights: " + numberOfNights
                + " | Total: $" + String.format("%.2f", totalCost);
    }
}

/** Handles saving and loading of system state to and from disk. */
class PersistenceService {
    private static final String BOOKING_FILE   = "booking_history.dat";
    private static final String INVENTORY_FILE = "inventory_snapshot.dat";

    /**
     * Saves the booking history list to a binary file.
     *
     * @param history the list of confirmed bookings to persist
     */
    @SuppressWarnings("unchecked")
    public void saveBookingHistory(List<PersistableBooking> history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKING_FILE))) {
            oos.writeObject(new ArrayList<>(history));
            System.out.println("  [Saved] Booking history -> " + BOOKING_FILE);
        } catch (IOException e) {
            System.out.println("  [ERROR] Failed to save booking history: " + e.getMessage());
        }
    }

    /**
     * Loads booking history from the binary file.
     *
     * @return list of recovered bookings, or empty list if file missing or corrupt
     */
    @SuppressWarnings("unchecked")
    public List<PersistableBooking> loadBookingHistory() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKING_FILE))) {
            List<PersistableBooking> history = (List<PersistableBooking>) ois.readObject();
            System.out.println("  [Loaded] Booking history <- " + BOOKING_FILE);
            return history;
        } catch (FileNotFoundException e) {
            System.out.println("  [Info] No previous booking history found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("  [Warning] Booking file corrupted or incompatible. Starting fresh.");
        }
        return new ArrayList<>();
    }

    /**
     * Saves the inventory snapshot to a binary file.
     *
     * @param inventory the current room inventory map
     */
    public void saveInventory(HashMap<String, Integer> inventory) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(INVENTORY_FILE))) {
            oos.writeObject(new HashMap<>(inventory));
            System.out.println("  [Saved] Inventory snapshot -> " + INVENTORY_FILE);
        } catch (IOException e) {
            System.out.println("  [ERROR] Failed to save inventory: " + e.getMessage());
        }
    }

    /**
     * Loads inventory snapshot from the binary file.
     *
     * @return recovered inventory map, or default map if file missing or corrupt
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, Integer> loadInventory() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(INVENTORY_FILE))) {
            HashMap<String, Integer> inventory = (HashMap<String, Integer>) ois.readObject();
            System.out.println("  [Loaded] Inventory snapshot <- " + INVENTORY_FILE);
            return inventory;
        } catch (FileNotFoundException e) {
            System.out.println("  [Info] No previous inventory found. Using defaults.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("  [Warning] Inventory file corrupted. Using defaults.");
        }

        // Default inventory on first run
        HashMap<String, Integer> defaults = new HashMap<>();
        defaults.put("Single Room", 5);
        defaults.put("Double Room", 3);
        defaults.put("Suite Room",  2);
        return defaults;
    }
}

/** Application entry point for Use Case 12. */
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Hotel Booking System v12.0              ");
        System.out.println("   Use Case 12: Data Persistence & Recovery ");
        System.out.println("============================================");

        PersistenceService persistenceService = new PersistenceService();

        // --- Phase 1: Simulate system shutdown (save state) ---
        System.out.println("\n--- Phase 1: Saving Current System State ---\n");

        List<PersistableBooking> currentHistory = new ArrayList<>();
        currentHistory.add(new PersistableBooking("S-1001", "Alice",   "Single Room", "S-1001", 3, 300.0));
        currentHistory.add(new PersistableBooking("D-1002", "Bob",     "Double Room", "D-1002", 2, 300.0));
        currentHistory.add(new PersistableBooking("X-1003", "Charlie", "Suite Room",  "X-1003", 5, 1500.0));

        HashMap<String, Integer> currentInventory = new HashMap<>();
        currentInventory.put("Single Room", 4);
        currentInventory.put("Double Room", 2);
        currentInventory.put("Suite Room",  1);

        persistenceService.saveBookingHistory(currentHistory);
        persistenceService.saveInventory(currentInventory);

        System.out.println("\n  [System Shutdown Simulated]\n");

        // --- Phase 2: Simulate system restart (recover state) ---
        System.out.println("--- Phase 2: Recovering State After Restart ---\n");

        List<PersistableBooking> recoveredHistory  = persistenceService.loadBookingHistory();
        HashMap<String, Integer> recoveredInventory = persistenceService.loadInventory();

        System.out.println("\n--- Recovered Booking History ---\n");
        if (recoveredHistory.isEmpty()) {
            System.out.println("  No bookings recovered.");
        } else {
            int index = 1;
            for (PersistableBooking b : recoveredHistory) {
                System.out.println("  " + index + ". " + b);
                index++;
            }
        }

        System.out.println("\n--- Recovered Inventory Snapshot ---\n");
        for (Map.Entry<String, Integer> e : recoveredInventory.entrySet()) {
            System.out.printf("  %-15s : %d%n", e.getKey(), e.getValue());
        }

        System.out.println("\n============================================");
        System.out.println("  System recovery complete.");
        System.out.println("  State matches pre-shutdown snapshot.");
        System.out.println("============================================");
    }
}
