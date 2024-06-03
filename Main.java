import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

public class Main {
    private static ArrayList<Event> allEvents = new ArrayList<>();
    private static ArrayList<Venue> allVenues = new ArrayList<>();
    private static ArrayList<Attendee> allAttendees = new ArrayList<>();


    // Encapsulation: Getters for private static lists
    public static ArrayList<Event> getAllEvents() {
        return allEvents;
    }
    
    public static ArrayList<Venue> getAllVenues() {
        return allVenues;
    }
    
    public static ArrayList<Attendee> getAllAttendees() {
        return allAttendees;
    }
    

    // Centralized methods to add entities to the lists
    public static void addEvent(Event event) {
        allEvents.add(event);
    }
    
    public static void addVenue(Venue venue) {
        Venue.addVenue(venue);
    }
    
    public static void addAttendee(Attendee attendee) {
        allAttendees.add(attendee);
    }

    public static void removeEvent(String eventName) {
   	 allEvents.removeIf(event -> event.getEventName().equalsIgnoreCase(eventName));
    }

    public static void removeVenue(Venue venueToRemove) {
        allVenues.remove(venueToRemove);
    }

    
    static {
        initializePredefinedVenues();
    }

    private static void initializePredefinedVenues() {
        allVenues = new ArrayList<>();
        allVenues.add(new Venue("Party Hall A", 25, 50));
        allVenues.add(new Venue("Party Hall B", 50, 100));
        allVenues.add(new Venue("Party Hall C", 75, 150));
        allVenues.add(new Venue("Party Hall D", 100, 200));
    }
    
    



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Deserialize data if files exist
        File eventsFile = new File("events.dat");
        File venuesFile = new File("venues.dat");
        File attendeesFile = new File("attendees.dat");

        if (eventsFile.exists()) {
            allEvents = new ArrayList<>(Event.deserializeEvents());
        }

        if (venuesFile.exists()) {
            allVenues = new ArrayList<>(Venue.deserializeVenues());
        }

        if (attendeesFile.exists()) {
            allAttendees = new ArrayList<>(Attendee.deserializeAttendees());
        }


        while (true) {
            try {
                System.out.println("\nWelcome to the Event Management System!");
                System.out.println("Please select your role:");
                System.out.println("1. Organizer\n2. Host\n3. Attendee\n4. Exit");
                System.out.print("Enter the number of your role: ");
    
                int roleNumber = Integer.parseInt(scanner.nextLine());
                processRoleSelection(roleNumber, scanner);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        
        }
    }

    // Method to process role selection, improving readability and separation of concerns
    private static void processRoleSelection(int roleNumber, Scanner scanner) {
        scanner = new Scanner(System.in);
        switch (roleNumber) {
            case 1:
                new Organizer().performTasks();
                break;
            case 2:
                new Host().performTasks();
                break;
            case 3:
                //new Attendee().performTasks();
                System.out.print("Enter your name: ");
                String attendeeName = scanner.nextLine();
                new Attendee(attendeeName).performTasks();
                break;
            case 4:
                Event.serializeEvents(new ArrayList<>(Main.getAllEvents())); // Explicit casting if necessary
                Venue.serializeVenues(new ArrayList<>(Main.getAllVenues())); // Explicit casting if necessary
                Attendee.serializeAttendees(new ArrayList<>(Main.getAllAttendees())); // Explicit casting if necessary
               
                System.out.println("Data saved. Exiting...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid selection. Please choose a valid role.");
                break;
        }
    }
}