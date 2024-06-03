import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class Host extends EventManager {
    private ArrayList<Event> upcomingEvents;

    public Host() {
        upcomingEvents = new ArrayList<>();
    }
    
    @Override
    public void performTasks() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome, Host!");
            System.out.println("What would you like to do as a Host?");
            System.out.println("1. View Event Details");
            System.out.println("2. Add an Upcoming Event");
            System.out.println("3. View Event Attendees");
            System.out.println("4. View Event Feedback");
            System.out.println("5. Exit");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    viewEvents();
                    break;
                case 2:
                    createEvent();
                    break;
                case 3:
                    viewEventAttendees();
                    break;
                case 4:
                    viewFeedback();
                    break;
                case 5:
                    System.out.println("Returning to the main menu...");
                    return;
                default:
                    System.out.println("Invalid option.");
                    break;
            }
        }
    }
    
    private void viewEventAttendees() {
        System.out.println("Enter the name of the event to view its attendees:");
        String eventName = scanner.nextLine();

        Event selectedEvent = findEventByName(eventName);
        if (selectedEvent == null) {
            System.out.println("Event not found.");
        } else {
            System.out.println("Attendees for " + selectedEvent.getEventName() + ":");
            for (Attendee attendee : selectedEvent.getAttendees()) {
                System.out.println("- " + attendee.getName());
            }
        }
    }
    
    
    

    public ArrayList<Event> getUpcomingEvents() {
        return upcomingEvents;
    }
    
    
    
    @Override
    public void viewEvents() {
        System.out.println("Enter the name of the event to view its details:");
        String eventName = scanner.nextLine();

        Event selectedEvent = findEventByName(eventName);
        if (selectedEvent == null) {
            System.out.println("Event not found.");
        } else {
            selectedEvent.displayEventDetails();
        }
    }

   
}
