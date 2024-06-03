import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String eventName;
    private LocalDate date;
    private LocalTime time;
    private Venue venue;
    private ArrayList<Attendee> attendees;
    private List<String> feedbackList;
    private boolean isPublic;
    private String accessCode;

    public Event(String eventName, LocalDate date, LocalTime time, Venue venue, boolean isPublic, String accessCode) {
       
        this.eventName = eventName;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.isPublic = isPublic;
        this.accessCode = accessCode;
        this.attendees = new ArrayList<>();
        this.feedbackList = new ArrayList<>();
    }
    
    public void addAttendee(Attendee attendee) {
        attendees.add(attendee);
        checkAndAssignVenue();
        upgradeVenueIfNeeded();
    }
    
    private void upgradeVenueIfNeeded() {
        if (attendees.size() > venue.getCapacity()) {
            Venue upgradedVenue = venue.upgradeVenue(attendees.size(), this.date);
            if (upgradedVenue != null) {
                this.venue = upgradedVenue;
                System.out.println("Venue upgraded to accommodate more attendees.");
            }
        }
    }

    
    public void addFeedback(String feedback) {
        feedbackList.add(feedback);
    }
    
    public boolean isPublic() {
        return isPublic;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public Venue getVenue() {
        return venue;
    }

    public ArrayList<Attendee> getAttendees() {
        return attendees;
    }

    
    
    private void checkAndAssignVenue() {
        boolean venueAssigned = false;
    
        // Check if the current venue can accommodate the attendees
        if (!this.venue.getName().equals("TBD") && this.venue.getCapacity() >= attendees.size()) {
            return; // Current venue is still suitable
        }
    
        // Try to find a suitable venue
        for (Venue v : Main.getAllVenues()) {
            if (v.getCapacity() >= attendees.size() && v.isAvailable(this.date)) {
                this.venue = v;
                venueAssigned = true;
                break;
            }
        }
    
        // If no suitable venue is found or the current venue is overloaded, upgrade
        if (!venueAssigned || this.venue.getCurrentAttendees() + 5 <= attendees.size()) {
            Venue upgradedVenue = this.venue.upgradeVenue(attendees.size(), this.date);
            if (upgradedVenue != null) {
                this.venue = upgradedVenue;
            } else if (this.venue.getName().equals("TBD")) {
                this.venue = Main.getAllVenues().get(0); // Assign the smallest venue
            }
        }
    
        // Update the current attendees count in the assigned venue
        this.venue.registerAttendees(attendees.size(), this.date);
    }

    public boolean registerAttendees(int attendees) {
        int remainingCapacity = venue.getCapacity() - venue.getCurrentAttendees();
        if (attendees > remainingCapacity) {
            if (venue.registerAttendees(attendees, this.date)) {
                return true;
            } else {
                return false; // Capacity exceeded even after upgrade attempt
            }
        } else {
            return venue.registerAttendees(attendees, this.date);
        }
    }
    
    public int getNumberOfAttendees() {
        return attendees.size();
    }
    
    public List<String> getFeedbackList() {
        return feedbackList;
    }

    public void displayFeedback() {
        if (feedbackList.isEmpty()) {
            System.out.println("No feedback available for this event.");
        } else {
            System.out.println("Feedback for " + eventName + ":");
            for (String feedback : feedbackList) {
                System.out.println("- " + feedback);
            }
        }
    }
    
    private void assignVenue() {
        // Check if the current venue can accommodate the attendees
        if (!this.venue.getName().equals("TBD") && this.venue.getCapacity() >= attendees.size()) {
            return; // Current venue is still suitable
        }

        // Try to find a suitable venue
        boolean venueAssigned = false;
        for (Venue v : Main.getAllVenues()) {
            if (v.getCapacity() >= attendees.size()) {
                this.venue = v;
                venueAssigned = true;
                break;
            }
        }

        // If no suitable venue is found or the current venue is overloaded, upgrade
        if (!venueAssigned || this.venue.getCurrentAttendees() <= attendees.size()) {
            Venue upgradedVenue = this.venue.upgradeVenue(attendees.size(), this.date);
            if (upgradedVenue != null) {
                this.venue = upgradedVenue;
            } else if (this.venue.getName().equals("TBD")) {
                this.venue = Main.getAllVenues().get(0); // Assign the smallest venue
            }
        }
        

        // Update the current attendees count in the assigned venue
        this.venue.registerAttendees(attendees.size(), this.date);
    }
    
    
    public void checkAndUpgradeVenueIfNeeded() {
        if (attendees.size() > venue.getCapacity()) {
            Venue upgradedVenue = venue.upgradeVenue(attendees.size(), this.date);
            if (upgradedVenue != null) {
                this.venue = upgradedVenue;
                // Update venue details as necessary
            }
        }
    }

    public void displayEventDetails() {
        System.out.println("--- Event Details ---");
        System.out.println("Event Name: " + eventName);
        System.out.println("Date: " + date);
        System.out.println("Time: " + time);
        System.out.println("Total Attendees: " + attendees.size());
        venue.displayVenueDetails();
    }
    
    
    // Serialization method
    public static void serializeEvents(List<Event> events) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("events.dat"))) {
            out.writeObject(new ArrayList<>(events)); // Ensure it's serializable
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Deserialization method
    public static List<Event> deserializeEvents() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("events.dat"))) {
            return (List<Event>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list in case of an error
        }
    }

}
