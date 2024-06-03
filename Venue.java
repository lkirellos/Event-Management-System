import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.*;
import java.io.*;
import java.io.Serializable;


public class Venue implements Serializable {
    private String name;
    private int capacity;
    private int budget;
    private int currentAttendees;
    private Set<LocalDate> bookedDates;
    private static HashMap<String, Integer> venueCapacities = new HashMap<>();
    private static HashMap<String, Integer> venueBudgets = new HashMap<>();
    private static List<Venue> venuesList = new ArrayList<>();
    private static final long serialVersionUID = 1L;

    
   static {
        venueCapacities.put("Party Hall A", 25);
        venueCapacities.put("Party Hall B", 50);
        venueCapacities.put("Party Hall C", 75);
        venueCapacities.put("Party Hall D", 100);

        venueBudgets.put("Party Hall A", 50);
        venueBudgets.put("Party Hall B", 100);
        venueBudgets.put("Party Hall C", 150);
        venueBudgets.put("Party Hall D", 200);

	for (String name : venueCapacities.keySet()) {
            int capacity = venueCapacities.get(name);
            int budget = venueBudgets.get(name);
            venuesList.add(new Venue(name, capacity, budget));
        }

        venuesList = initializeVenues();
    }

    private static List<Venue> initializeVenues() {
        List<Venue> list = new ArrayList<>();
        for (String name : venueCapacities.keySet()) {
            int capacity = venueCapacities.get(name);
            int budget = venueBudgets.get(name);
            list.add(new Venue(name, capacity, budget));
        }
        return list;
    }


    public static List<Venue> getAllVenues() {
        return new ArrayList<>(venuesList); // Return a copy of the venues list
    }
    
    public static void addVenue(Venue newVenue) {
        venuesList.add(newVenue);
    }

    public Venue(String name, int capacity, int budget) {
        this.name = name;
        this.capacity = capacity;
        this.budget = budget;
        this.currentAttendees = 0;
        this.bookedDates = new HashSet<>();
    }
    
    

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }
    
    public int getBudget() {
        return budget;
    }

    public int getCurrentAttendees() {
        return currentAttendees;
    }
    
    public boolean isAvailable(LocalDate date) {
        return !bookedDates.contains(date); // Return true if date is not in the set
    }

    public void bookDate(LocalDate date) {
        bookedDates.add(date); // Add the date to the set
    }
    
    
    public static int getBudgetForVenue(String venueName) {
        switch (venueName) {
            case "Party Hall A":
                return 50;
            case "Party Hall B":
                return 100;
            case "Party Hall C":
                return 150;
            case "Party Hall D":
                return 200;
            default:
                return 0; // Default budget if the venue name does not match
	}
     } 


    public boolean registerAttendees(int attendees, LocalDate eventDate) {
        if (attendees <= capacity - currentAttendees) {
            currentAttendees += attendees;
            return true;
        } else {
            // Attempt to upgrade the venue if the capacity is insufficient
            Venue upgradedVenue = upgradeVenueBasedOnCapacity(attendees, eventDate);
            if (upgradedVenue != null) {
                // Update current venue with the upgraded one
                this.name = upgradedVenue.getName();
                this.capacity = upgradedVenue.getCapacity();
                this.currentAttendees += attendees;
                return true;
            }
            return false; // Unable to accommodate attendees even after upgrade attempt
        }
    }

    private Venue upgradeVenueBasedOnCapacity(int requiredCapacity, LocalDate eventDate) {
        // Find an available venue with a capacity greater than the required capacity
        for (Venue venue : venuesList) {
            if (venue.getCapacity() >= requiredCapacity && venue.isAvailable(eventDate)) {
                return venue;
            }
        }
        return null; // No suitable upgrade found
    }



    public Venue upgradeVenue(int requiredCapacity, LocalDate eventDate) {
        // Sort venues by capacity to find the next larger venue
        List<Venue> sortedVenues = new ArrayList<>(venuesList);
        sortedVenues.sort(Comparator.comparingInt(Venue::getCapacity));

        for (Venue venue : sortedVenues) {
            if (venue.getCapacity() > this.capacity && venue.isAvailable(eventDate)) {
                return venue;
            }
        }
        return null; // No suitable larger venue available
    }

    private static Venue findVenueByName(String name) {
        return venuesList.stream().filter(v -> v.getName().equals(name)).findFirst().orElse(null);
    }

    public void displayVenueDetails() {
        System.out.println("--- Venue Details ---");
        System.out.println("Venue: " + name);
        System.out.println("Capacity: " + capacity);
    }

    public static void removeVenue(Venue venueToRemove) {
        venuesList.remove(venueToRemove);
        serializeVenues(venuesList); // Serialize after modification
    }
    
    
    public static void serializeVenues(List<Venue> venues) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("venues.dat"))) {
            out.writeObject(venues);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<Venue> deserializeVenues() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("venues.dat"))) {
            return (List<Venue>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
