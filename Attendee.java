import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.stream.Collectors;
import java.io.*;

class Attendee implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private List<Event> registeredEvents;
    private transient Scanner scanner;
    
    public Attendee(String name) {
        this.name = name;
        this.registeredEvents = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }
    public String getName() {
        return name;
    }
    
    public void performTasks() {
	if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        while (true) {
            System.out.println("\nWelcome, Attendee!");
            System.out.println("1. Register for an Event");
            System.out.println("2. Write Feedback");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerForEvent();
                    break;
                case 2:
                    writeFeedback();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }
    
    
    
    private void registerForEvent() {
        // Displaying available public events
        List<Event> publicEvents = Main.getAllEvents().stream().filter(Event::isPublic).collect(Collectors.toList());
        if (!publicEvents.isEmpty()) {
            System.out.println("Available Public Events:");
            for (int i = 0; i < publicEvents.size(); i++) {
                Event event = publicEvents.get(i);
                System.out.println((i + 1) + ". " + event.getEventName() + " - Date: " + event.getDate() + ", Time: " + event.getTime());
            }
        } else {
            System.out.println("No public events available.");
        }

        // Ask if the attendee wants to register for a private event
        System.out.println("Do you want to register for a private event? (yes/no): ");
        String privateEventResponse = scanner.nextLine().trim();

        if (privateEventResponse.equalsIgnoreCase("yes")) {
            registerForPrivateEvent();
        } else {
            registerForPublicEvent(publicEvents);
        }
    }

    private void registerForPublicEvent(List<Event> publicEvents) {
        if (publicEvents.isEmpty()) {
            System.out.println("No public events to register for.");
            return;
        }

        System.out.println("Enter the number of the public event you want to register for (0 to exit):");
        try {
            int eventChoice = Integer.parseInt(scanner.nextLine());
            if (eventChoice == 0) {
                return;
            }

            if (eventChoice > 0 && eventChoice <= publicEvents.size()) {
                Event selectedEvent = publicEvents.get(eventChoice - 1);
                if (registeredEvents.contains(selectedEvent)) {
                    System.out.println("You are already registered for this event.");
                } else {
                    selectedEvent.addAttendee(this);
                    Main.addAttendee(this);
                    registeredEvents.add(selectedEvent);
                    System.out.println("Successfully registered for event: " + selectedEvent.getEventName());
                    System.out.println("Venue: " + selectedEvent.getVenue().getName());
                }
            } else {
                System.out.println("Invalid event selection. Please enter a valid number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
    
    private void registerForPrivateEventIfDesired() {
        System.out.println("Do you want to register for a private event? (yes/no): ");
        String response = scanner.nextLine().trim();
        if (response.equalsIgnoreCase("yes")) {
            registerForPrivateEvent();
        }
    }


    
    
    private void registerForPrivateEvent() {
        System.out.println("Enter the access code of the private event:");
        String accessCode = scanner.nextLine().trim();
    
        Event event = Main.getAllEvents().stream()
                          .filter(e -> !e.isPublic() && e.getAccessCode().equals(accessCode))
                          .findFirst()
                          .orElse(null);
    
        if (event == null) {
            System.out.println("Invalid access code or event not found.");
        } else {
            // Check if the attendee is already registered
            if (event.getAttendees().contains(this)) {
                System.out.println("You are already registered for this event.");
                return;
            }
    
            // Register the attendee for the event
            event.addAttendee(this);
            Main.addAttendee(this); 
            registeredEvents.add(event); // Adding the event to the attendee's list of registered events
            System.out.println("Successfully registered for the private event: " + event.getEventName());
            displayEventDetails(event);
        }
    }
    
    
    private void displayEventDetails(Event event) {
        System.out.println("Event Details:");
        System.out.println("Name: " + event.getEventName());
        System.out.println("Date: " + event.getDate());
        System.out.println("Time: " + event.getTime());
        System.out.println("Venue: " + event.getVenue().getName());
    }


    private void writeFeedback() {
        System.out.println("Enter the name of the event you want to provide feedback for:");
        String eventName = scanner.nextLine();

        Event event = findEventByName(eventName);
        if (event == null) {
            System.out.println("This event does not exist.");
            return;
        }

        System.out.println("Enter your feedback:");
        String feedback = scanner.nextLine();
        event.addFeedback(feedback);
        System.out.println("Thank you for your feedback!");
    }

    private Event findEventByName(String eventName) {
        for (Event event : Main.getAllEvents()) {
            if (event.getEventName().equalsIgnoreCase(eventName)) {
                return event;
            }
        }
        return null;
    }
    
    public static void serializeAttendees(List<Attendee> attendees) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("attendees.dat"))) {
            out.writeObject(attendees);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static List<Attendee> deserializeAttendees() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("attendees.dat"))) {
            return (List<Attendee>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }




}
