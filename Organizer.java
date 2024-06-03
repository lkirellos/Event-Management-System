import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Organizer extends EventManager {
    private transient Scanner scanner;

    public Organizer() {
        this.scanner = new Scanner(System.in);
    }
    
    
    @Override
    public void performTasks() {
        while (true) {
            System.out.println("\nWelcome, Organizer!");
            System.out.println("What would you like to do as an Organizer?");
            System.out.println("1. Create an Event");
            System.out.println("2. View Events");
            System.out.println("3. Add Venue");
            System.out.println("4. View Event Feedback");
            System.out.println("5. View Total Budget for Booked Venues");
	    System.out.println("6. Remove an Event");
            System.out.println("7. Remove Venue");
	    System.out.println("8. Exit");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    createEvent();
                    break;
                case 2:
                    viewEvents();
                    break;
                case 3:
                    createVenue();
                    break;
                case 4:
                    displayAllEventFeedback();
                    break;
                case 5:
                    viewTotalBudget();
                    break;
                case 6:
		    removeEventOption();
		    break;
		case 7:
		    removeVenue();
		    break;
		case 8:
                    System.out.println("Returning to the main menu...");
                    return;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }

    

    
    
    private void createVenue() {
        displayAllVenues();
        System.out.println("Enter venue name (0 to exit):");
        String venueName = scanner.nextLine().trim();

	if ("0".equals(venueName)) {
        	System.out.println("Exiting venue creation...");
        	return;
    	}
    
        if (isVenueNameDuplicate(venueName)) {
            System.out.println("Venue '" + venueName + "' already exists.");
            return;
        }
    
        System.out.println("Enter venue capacity:");
        int venueCapacity = getValidNumberInput("Invalid number format for capacity.");
    
        System.out.println("Enter venue budget:");
        int venueBudget = getValidNumberInput("Invalid number format for budget.");
    
        Venue newVenue = new Venue(venueName, venueCapacity, venueBudget);
        Venue.addVenue(newVenue);
        System.out.println("Venue '" + venueName + "' added successfully!");
	Venue.serializeVenues(Venue.getAllVenues());
    
        displayAllVenues();
    }
    
    private boolean isVenueNameDuplicate(String venueName) {
        return Venue.getAllVenues().stream()
            .anyMatch(venue -> venue.getName().equalsIgnoreCase(venueName));
    }
    
    private int getValidNumberInput(String errorMessage) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
            }
        }
    }
    
    private void displayAllVenues() {
        System.out.println("Available Venues:");
        for (Venue venue : Venue.getAllVenues()) {
            System.out.println(venue.getName() + " - Capacity: " + venue.getCapacity() + " - Budget: " + venue.getBudget());
        }
    }


    
    @Override
    public void viewEvents() {
        ArrayList<Event> events = Main.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available.");
        } else {
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                System.out.println((i + 1) + ". " + event.getEventName() + 
                                   " - Date: " + event.getDate() + 
                                   ", Time: " + event.getTime());
            }
            System.out.println("Enter the number of the event to view more details (0 to exit):");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice > 0 && choice <= events.size()) {
                events.get(choice - 1).displayEventDetails();
            }
        }
    }
    
    
    private void displayAllEventFeedback() {
        System.out.println("Event Feedback:");
        for (Event event : Main.getAllEvents()) {
            System.out.println("Event: " + event.getEventName());
            if (event.getFeedbackList().isEmpty()) {
                System.out.println("  No feedback available for this event.");
            } else {
                for (String feedback : event.getFeedbackList()) {
                    System.out.println("  - " + feedback);
                }
            }
        }
    }
    
    
    public void displayTotalBudget() {
        int totalBudget = Main.getAllEvents().stream().mapToInt(event -> event.getVenue().getBudget()).sum();
        System.out.println("Total budget for all events: " + totalBudget);
    }
    
    private void viewTotalBudget() {
        int totalBudget = 0;
        for (Event event : Main.getAllEvents()) {
            totalBudget += event.getVenue().getBudget();
        }
        System.out.println("Total budget for booked venues: $" + totalBudget);
    }

    private void removeEventOption() {
   	 System.out.println("Enter the name of the event to remove:");
   	 String eventName = scanner.nextLine();
   	 Main.removeEvent(eventName);
   	 System.out.println("Event removed successfully.");
    }

    public void removeVenue() {
        if (Venue.getAllVenues().isEmpty()) {
            System.out.println("There are no venues to remove.");
            return;
        }

        displayAllVenuesWithNumbers();
        System.out.println("Enter the number of the venue you want to remove (0 to exit):");

        int venueNumber;
        try {
            venueNumber = Integer.parseInt(scanner.nextLine());
            if (venueNumber == 0) {
                System.out.println("Exiting venue removal...");
                return;
            }

            if (venueNumber < 1 || venueNumber > Venue.getAllVenues().size()) {
                System.out.println("Invalid venue number. Please enter a valid number.");
                return;
            }

            Venue venueToRemove = Venue.getAllVenues().get(venueNumber - 1);
            Venue.removeVenue(venueToRemove);
	    Venue.serializeVenues(Venue.getAllVenues());
            System.out.println("Venue '" + venueToRemove.getName() + "' removed successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    private void displayAllVenuesWithNumbers() {
        List<Venue> allVenues = Venue.getAllVenues();
        System.out.println("Available Venues:");
        for (int i = 0; i < allVenues.size(); i++) {
            Venue venue = allVenues.get(i);
            System.out.println((i + 1) + ". " + venue.getName() + " - Capacity: " + venue.getCapacity() + " - Budget: " + venue.getBudget());
        }
    }


    

    
}
