import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public abstract class EventManager implements Role {
    protected transient Scanner scanner;

    public EventManager() {
        this.scanner = new Scanner(System.in);
    }

    protected void createEvent() {
        System.out.print("Enter event name: ");
        String eventName = scanner.nextLine();

        LocalDate date = checkUserDate();
        LocalTime time = checkUserTime();
        
        System.out.println("Is this event public? (yes/no): ");
        boolean isPublic = scanner.nextLine().trim().equalsIgnoreCase("yes");
        String accessCode = "";
    
        if (!isPublic) {
            System.out.println("Enter an access code for your private event: ");
            accessCode = scanner.nextLine().trim();
        }
        
        Venue selectedVenue = selectVenue(date); // Call method to select a venue

        if (selectedVenue == null) {
            System.out.println("Event creation cancelled. Returning to the main menu...");
            return;
        }
    
        // Proceed with event creation using the selected venue
        Event newEvent = new Event(eventName, date, time, selectedVenue, isPublic, accessCode);
        Main.addEvent(newEvent);
        System.out.println("Event '" + eventName + "' created successfully.");
        
    }
    
    
    
    private Venue selectVenue(LocalDate date) {
        Venue selectedVenue = null;
        while (selectedVenue == null) {
            System.out.println("Select a venue:");
            List<Venue> allVenues = Venue.getAllVenues();
            for (int i = 0; i < allVenues.size(); i++) {
                Venue venue = allVenues.get(i);
                System.out.printf("%d. %s - Capacity: %d, Budget: %d\n", i + 1, venue.getName(), venue.getCapacity(), venue.getBudget());
            }
            System.out.println("Enter the number of the venue (0 to exit):");

            try {
                int venueChoice = Integer.parseInt(scanner.nextLine());
                if (venueChoice == 0) {
                    break; // Exit the venue selection
                }

                if (venueChoice < 1 || venueChoice > allVenues.size()) {
                    System.out.println("Invalid venue selection. Please select a valid number.");
                } else {
                    Venue venue = allVenues.get(venueChoice - 1);
                    if (venue.isAvailable(date)) {
                        venue.bookDate(date);
                        selectedVenue = venue;
                    } else {
                        System.out.println("Venue not available on selected date. Please select a different venue.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return selectedVenue;
    }
    
    public abstract void viewEvents();
 
    protected void viewFeedback() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the event you want to view feedback for:");
        String eventName = scanner.nextLine();

        Event event = findEventByName(eventName);
        if (event == null) {
            System.out.println("Event not found.");
        } else {
            displayEventFeedback(event);
        }
    }

    protected Event findEventByName(String eventName) {
        List<Event> events = Main.getAllEvents();
        for (Event event : events) {
            if (event.getEventName().equalsIgnoreCase(eventName)) {
                return event;
            }
        }
        return null; // Event not found
    }

    protected void displayEventFeedback(Event event) {
        System.out.println("Feedback for " + event.getEventName() + ":");
        List<String> feedbackList = event.getFeedbackList();

        if (feedbackList.isEmpty()) {
            System.out.println("  No feedback available for this event.");
        } else {
            for (String feedback : feedbackList) {
                System.out.println("  - " + feedback);
            }
        }
    }
    
    
    protected LocalDate checkUserDate() {
        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter a date (yyyy-MM-dd format): ");
            String userInput = scanner.nextLine();
            try {
                date = LocalDate.parse(userInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                if (date.isBefore(LocalDate.now())) {
                    System.out.println("Invalid date! Please enter an upcoming date:");
                    date = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter a valid date.");
            }
        }
        return date;
    }
    
    protected LocalTime checkUserTime() {
        LocalTime time = null;
        while (time == null) {
            System.out.print("Enter time (HH:mm): ");
            String timeString = scanner.nextLine();
            try {
                time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format. Please enter a valid time.");
            }
        }
        return time;
    }



    
}
