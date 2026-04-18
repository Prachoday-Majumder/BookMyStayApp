/**
 * UseCase2RoomInitialization - Demonstrates object modeling through inheritance and abstraction.
 * Introduces abstract classes, inheritance, polymorphism, encapsulation, and
 * static availability representation.
 *
 * @author Prachoday
 * @version 2.0
 */

/**
 * Abstract base class representing a generic hotel room.
 * Defines common attributes and enforces a consistent structure for all room types.
 */
abstract class Room {
    private String roomType;
    private int numberOfBeds;
    private double sizeSqFt;
    private double pricePerNight;

    public Room(String roomType, int numberOfBeds, double sizeSqFt, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.sizeSqFt = sizeSqFt;
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType()      { return roomType; }
    public int getNumberOfBeds()     { return numberOfBeds; }
    public double getSizeSqFt()      { return sizeSqFt; }
    public double getPricePerNight() { return pricePerNight; }

    /**
     * Abstract method to describe the room's amenities.
     * Subclasses must provide their own implementation.
     */
    public abstract String describeAmenities();

    public void displayInfo() {
        System.out.println("  Room Type       : " + roomType);
        System.out.println("  Number of Beds  : " + numberOfBeds);
        System.out.println("  Size            : " + sizeSqFt + " sqft");
        System.out.printf ("  Price Per Night : $%.2f%n", pricePerNight);
        System.out.println("  Amenities       : " + describeAmenities());
    }
}

/** Represents a Single Room – suitable for solo travellers. */
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200.0, 100.0);
    }

    @Override
    public String describeAmenities() {
        return "WiFi, TV, Work Desk";
    }
}

/** Represents a Double Room – suitable for couples or small families. */
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350.0, 150.0);
    }

    @Override
    public String describeAmenities() {
        return "WiFi, TV, Mini Bar, Balcony";
    }
}

/** Represents a Suite Room – a luxury option for premium guests. */
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 600.0, 300.0);
    }

    @Override
    public String describeAmenities() {
        return "WiFi, Smart TV, Jacuzzi, Private Balcony, Butler Service";
    }
}

/** Application entry point for Use Case 2. */
public class UseCase2RoomInitialization {

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Hotel Booking System v2.0               ");
        System.out.println("   Use Case 2: Room Types & Availability    ");
        System.out.println("============================================");

        // Static availability variables – intentionally scattered to show limitations
        int singleRoomAvailable = 5;
        int doubleRoomAvailable = 3;
        int suiteRoomAvailable  = 2;

        // Polymorphic array of Room references
        Room[] rooms = { new SingleRoom(), new DoubleRoom(), new SuiteRoom() };
        int[]  avail = { singleRoomAvailable, doubleRoomAvailable, suiteRoomAvailable };

        System.out.println("\n--- Available Room Types ---\n");
        for (int i = 0; i < rooms.length; i++) {
            rooms[i].displayInfo();
            System.out.println("  Available Rooms : " + avail[i]);
            System.out.println();
        }

        System.out.println("============================================");
        System.out.println("  Room initialization complete.");
        System.out.println("============================================");
    }
}
