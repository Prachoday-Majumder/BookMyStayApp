import java.util.HashMap;
import java.util.Map;

/**
 * UseCase3InventorySetup - Introduces centralized inventory management using HashMap.
 * Replaces scattered availability variables with a single source of truth.
 *
 * @author Prachoday
 * @version 3.0
 */

/** Centralized room inventory backed by a HashMap. */
class RoomInventory {
    private HashMap<String, Integer> inventory;

    /**
     * Initializes the inventory with predefined room types and their counts.
     */
    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room",  2);
    }

    /**
     * Returns the available count for a specific room type.
     *
     * @param roomType the type of room to check
     * @return available room count, or 0 if room type is unknown
     */
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    /**
     * Updates the available count for a specific room type.
     *
     * @param roomType the type of room to update
     * @param count    the new available count
     */
    public void setAvailability(String roomType, int count) {
        if (inventory.containsKey(roomType)) {
            inventory.put(roomType, count);
        } else {
            System.out.println("  [Warning] Unknown room type: " + roomType);
        }
    }

    /** Prints the current state of the entire inventory. */
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---\n");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.printf("  %-15s : %d available%n", entry.getKey(), entry.getValue());
        }
        System.out.println();
    }
}

/** Application entry point for Use Case 3. */
public class UseCase3InventorySetup {

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Hotel Booking System v3.0               ");
        System.out.println("   Use Case 3: Centralized Inventory        ");
        System.out.println("============================================");

        RoomInventory inventory = new RoomInventory();
        inventory.displayInventory();

        // Demonstrating a controlled update
        System.out.println("  [Update] Reducing Single Room availability by 1 (simulated booking)...");
        int current = inventory.getAvailability("Single Room");
        inventory.setAvailability("Single Room", current - 1);
        inventory.displayInventory();

        System.out.println("============================================");
        System.out.println("  Inventory setup complete.");
        System.out.println("============================================");
    }
}
