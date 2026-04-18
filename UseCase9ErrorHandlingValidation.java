import java.util.HashMap;

/**
 * UseCase9ErrorHandlingValidation - Introduces structured validation and custom exceptions.
 * Demonstrates fail-fast design, graceful failure handling, and system state protection.
 *
 * @author Prachoday
 * @version 9.0
 */

/** Thrown when a guest provides an unknown or invalid room type. */
class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(String roomType) {
        super("Invalid room type: '" + roomType + "'. Please choose Single Room, Double Room, or Suite Room.");
    }
}

/** Thrown when a booking request exceeds available inventory. */
class InsufficientInventoryException extends Exception {
    public InsufficientInventoryException(String roomType, int requested, int available) {
        super("Insufficient inventory for '" + roomType + "'. Requested: " + requested
                + ", Available: " + available + ".");
    }
}

/** Thrown when guest name or nights input is invalid. */
class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}

/** Validates all booking inputs and system state before processing proceeds. */
class InvalidBookingValidator {
    private static final String[] VALID_ROOM_TYPES = {"Single Room", "Double Room", "Suite Room"};
    private HashMap<String, Integer> inventory;

    public InvalidBookingValidator() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room",  0);
    }

    /**
     * Validates that the guest name is not null or blank.
     *
     * @param guestName the name to validate
     * @throws InvalidInputException if name is null or blank
     */
    public void validateGuestName(String guestName) throws InvalidInputException {
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidInputException("Guest name cannot be null or empty.");
        }
    }

    /**
     * Validates that the number of nights is a positive integer.
     *
     * @param nights the night count to validate
     * @throws InvalidInputException if nights is less than 1
     */
    public void validateNights(int nights) throws InvalidInputException {
        if (nights < 1) {
            throw new InvalidInputException("Number of nights must be at least 1. Provided: " + nights);
        }
    }

    /**
     * Validates that the room type is one of the recognized types.
     * Note: Case-sensitive validation as per requirement.
     *
     * @param roomType the room type to validate
     * @throws InvalidRoomTypeException if the room type is not recognized
     */
    public void validateRoomType(String roomType) throws InvalidRoomTypeException {
        for (String valid : VALID_ROOM_TYPES) {
            if (valid.equals(roomType)) return;
        }
        throw new InvalidRoomTypeException(roomType);
    }

    /**
     * Validates that sufficient inventory exists for the requested room type.
     *
     * @param roomType  the type of room requested
     * @param requested number of rooms requested (typically 1)
     * @throws InsufficientInventoryException if availability is below requested count
     */
    public void validateAvailability(String roomType, int requested) throws InsufficientInventoryException {
        int available = inventory.getOrDefault(roomType, 0);
        if (available < requested) {
            throw new InsufficientInventoryException(roomType, requested, available);
        }
    }

    /**
     * Runs all validations as a single pipeline. Fails fast on the first error.
     *
     * @param guestName guest's name
     * @param roomType  requested room type
     * @param nights    number of nights
     * @throws InvalidInputException          on invalid name or nights
     * @throws InvalidRoomTypeException       on unrecognized room type
     * @throws InsufficientInventoryException on unavailable inventory
     */
    public void validate(String guestName, String roomType, int nights)
            throws InvalidInputException, InvalidRoomTypeException, InsufficientInventoryException {
        validateGuestName(guestName);
        validateNights(nights);
        validateRoomType(roomType);
        validateAvailability(roomType, 1);
    }
}

/** Application entry point for Use Case 9. */
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Hotel Booking System v9.0               ");
        System.out.println("   Use Case 9: Error Handling & Validation  ");
        System.out.println("============================================");

        InvalidBookingValidator validator = new InvalidBookingValidator();

        // Test scenarios
        String[][] testCases = {
            {"Alice",   "Single Room", "3"},   // Valid
            {"",        "Double Room", "2"},   // Empty name
            {"Charlie", "SUITE ROOM",  "1"},   // Wrong case (invalid type)
            {"Diana",   "Suite Room",  "2"},   // No availability
            {"Ethan",   "Single Room", "0"},   // Invalid nights
            {"Fiona",   "Penthouse",   "1"},   // Unknown room type
        };

        System.out.println("\n--- Validating Booking Requests ---\n");

        for (String[] tc : testCases) {
            String name     = tc[0];
            String roomType = tc[1];
            int nights      = Integer.parseInt(tc[2]);

            try {
                validator.validate(name, roomType, nights);
                System.out.println("  [VALID]   Guest: '" + name + "' | Room: " + roomType + " | Nights: " + nights);
            } catch (InvalidInputException e) {
                System.out.println("  [ERROR]   InvalidInputException       : " + e.getMessage());
            } catch (InvalidRoomTypeException e) {
                System.out.println("  [ERROR]   InvalidRoomTypeException    : " + e.getMessage());
            } catch (InsufficientInventoryException e) {
                System.out.println("  [ERROR]   InsufficientInventoryException: " + e.getMessage());
            }
        }

        System.out.println("\n  [Note] System continued running safely after all errors.");
        System.out.println("\n============================================");
        System.out.println("  Validation complete.");
        System.out.println("============================================");
    }
}
