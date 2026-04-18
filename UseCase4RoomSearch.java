import java.util.HashMap;
import java.util.Map;

/**
 * UseCase4RoomSearch - Enables read-only room search and availability check.
 * Reinforces safe data access and clear separation of responsibilities.
 *
 * @author Prachoday
 * @version 4.0
 */

/** Represents a hotel room with pricing and amenity info. */
class RoomInfo {
    private String roomType;
    private int numberOfBeds;
    private double sizeSqFt;
    private double pricePerNight;
    private String amenities;

    public RoomInfo(String roomType, int numberOfBeds, double sizeSqFt, double pricePerNight, String amenities) {
        this.roomType      = roomType;
        this.numberOfBeds  = numberOfBeds;
        this.sizeSqFt      = sizeSqFt;
        this.pricePerNight = pricePerNight;
        this.amenities     = amenities;
    }

    public String getRoomType()      { return roomType; }
    public double getPricePerNight() { return pricePerNight; }

    public void display() {
        System.out.println("  Room Type       : " + roomType);
        System.out.println("  Beds            : " + numberOfBeds);
        System.out.println("  Size            : " + sizeSqFt + " sqft");
        System.out.printf ("  Price Per Night : $%.2f%n", pricePerNight);
        System.out.println("  Amenities       : " + amenities);
    }
}

/** Read-only search service that queries inventory and room domain objects. */
class RoomSearchService {
    private HashMap<String, Integer> inventory;
    private HashMap<String, RoomInfo> roomCatalog;

    public RoomSearchService() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room",  0);  // intentionally 0 to show filtering

        roomCatalog = new HashMap<>();
        roomCatalog.put("Single Room", new RoomInfo("Single Room", 1, 200.0, 100.0, "WiFi, TV, Work Desk"));
        roomCatalog.put("Double Room", new RoomInfo("Double Room", 2, 350.0, 150.0, "WiFi, TV, Mini Bar, Balcony"));
        roomCatalog.put("Suite Room",  new RoomInfo("Suite Room",  3, 600.0, 300.0, "WiFi, Smart TV, Jacuzzi, Butler Service"));
    }

    /**
     * Searches and displays only room types with availability > 0.
     * Does NOT modify inventory state.
     */
    public void searchAvailableRooms() {
        System.out.println("\n--- Available Rooms (Read-Only Search) ---\n");
        boolean found = false;

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            String roomType = entry.getKey();
            int available   = entry.getValue();

            // Defensive check: only display rooms that are available
            if (available > 0 && roomCatalog.containsKey(roomType)) {
                roomCatalog.get(roomType).display();
                System.out.println("  Available Count : " + available);
                System.out.println();
                found = true;
            }
        }

        if (!found) {
            System.out.println("  No rooms are currently available.");
        }
    }
}

/** Application entry point for Use Case 4. */
public class UseCase4RoomSearch {

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Hotel Booking System v4.0               ");
        System.out.println("   Use Case 4: Room Search & Availability   ");
        System.out.println("============================================");

        RoomSearchService searchService = new RoomSearchService();
        searchService.searchAvailableRooms();

        System.out.println("============================================");
        System.out.println("  Search complete. Inventory unchanged.");
        System.out.println("============================================");
    }
}
