import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UseCase7AddOnServiceSelection - Extends booking model with optional add-on services.
 * Demonstrates Map-List combination and cost aggregation without touching core booking logic.
 *
 * @author Prachoday
 * @version 7.0
 */

/** Represents an optional add-on service for a reservation. */
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost        = cost;
    }

    public String getServiceName() { return serviceName; }
    public double getCost()        { return cost; }

    @Override
    public String toString() {
        return serviceName + " ($" + String.format("%.2f", cost) + ")";
    }
}

/** Manages the association between reservation IDs and their selected add-on services. */
class AddOnServiceManager {
    // One-to-many: reservationId -> list of selected services
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    /**
     * Attaches a service to a reservation.
     *
     * @param reservationId the unique identifier for the reservation
     * @param service       the add-on service to attach
     */
    public void addService(String reservationId, AddOnService service) {
        reservationServices.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
        System.out.println("  [Added] " + service + " -> Reservation: " + reservationId);
    }

    /**
     * Calculates total additional cost for a specific reservation.
     *
     * @param reservationId the reservation to calculate for
     * @return total cost of all attached services
     */
    public double getTotalServiceCost(String reservationId) {
        List<AddOnService> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());
        double total = 0.0;
        for (AddOnService s : services) {
            total += s.getCost();
        }
        return total;
    }

    /** Displays all reservations and their selected services with total cost. */
    public void displayAllServices() {
        System.out.println("\n--- Add-On Services per Reservation ---\n");
        for (Map.Entry<String, List<AddOnService>> entry : reservationServices.entrySet()) {
            String reservationId       = entry.getKey();
            List<AddOnService> services = entry.getValue();

            System.out.println("  Reservation ID : " + reservationId);
            System.out.println("  Services:");
            for (AddOnService s : services) {
                System.out.println("    - " + s);
            }
            System.out.printf("  Total Add-On Cost : $%.2f%n", getTotalServiceCost(reservationId));
            System.out.println();
        }
    }
}

/** Application entry point for Use Case 7. */
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Hotel Booking System v7.0               ");
        System.out.println("   Use Case 7: Add-On Service Selection     ");
        System.out.println("============================================");

        // Predefined service catalog
        AddOnService breakfast   = new AddOnService("Breakfast Buffet",      25.00);
        AddOnService airportPick = new AddOnService("Airport Pickup",         40.00);
        AddOnService spa         = new AddOnService("Spa Session",            80.00);
        AddOnService laundry     = new AddOnService("Laundry Service",        15.00);
        AddOnService cityTour    = new AddOnService("City Tour Package",      60.00);

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        System.out.println("\n--- Attaching Services to Reservations ---\n");
        serviceManager.addService("S-1001", breakfast);
        serviceManager.addService("S-1001", airportPick);
        serviceManager.addService("D-1002", breakfast);
        serviceManager.addService("D-1002", spa);
        serviceManager.addService("D-1002", cityTour);
        serviceManager.addService("X-1003", laundry);

        serviceManager.displayAllServices();

        System.out.println("  [Note] Core booking and inventory state unchanged.");
        System.out.println("\n============================================");
        System.out.println("  Add-on service selection complete.");
        System.out.println("============================================");
    }
}
