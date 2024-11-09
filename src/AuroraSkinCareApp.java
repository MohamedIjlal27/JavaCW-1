import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AuroraSkinCareApp {
    private static Clinic clinic = new Clinic();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeDoctors();
        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline
            switch (choice) {
                case 1 -> makeAppointment();
                case 2 -> updateAppointment();
                case 3 -> viewAppointmentsByDay();
                case 4 -> searchAppointment();
                case 5 -> generateInvoice();
                case 6 -> {
                    System.out.println("Exiting application.");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void initializeDoctors() {
        clinic.addDoctor(new Doctor("Dr. Smith"));
        clinic.addDoctor(new Doctor("Dr. Brown"));
    }

    private static void displayMenu() {
        System.out.println("\n--- Aurora Skin Care Clinic ---");
        System.out.println("1. Make Appointment");
        System.out.println("2. Update Appointment");
        System.out.println("3. View Appointments by Date");
        System.out.println("4. Search for Appointment");
        System.out.println("5. Generate Invoice");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private static void makeAppointment() {
        // Input Patient Details with Validation
        String nic = getInputWithValidation(
                "Enter Patient NIC (9 or 12 digits): ",
                "^[0-9]{9}|[0-9]{12}$",
                "NIC not valid. NIC should be either 9 or 12 digits without letters or special characters."
        );

        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine();

        String email = getInputWithValidation(
                "Enter Patient Email: ",
                "^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$",
                "Email not valid. Email should be in the format example@domain.com."
        );

        String phone = getInputWithValidation(
                "Enter Patient Phone (10 digits): ",
                "^[0-9]{10}$",
                "Phone number not valid. Phone number should be exactly 10 digits."
        );

        Patient patient = new Patient(nic, name, email, phone);

        // Select Doctor
        Doctor doctor = null;
        while (doctor == null) {
            System.out.println("Available Doctors:");
            for (int i = 0; i < clinic.getDoctors().size(); i++) {
                System.out.println((i + 1) + ". " + clinic.getDoctors().get(i).getName());
            }
            System.out.print("Select Doctor by number, or enter 0 to cancel: ");

            if (scanner.hasNextInt()) {
                int doctorIndex = scanner.nextInt() - 1;
                scanner.nextLine();

                if (doctorIndex == -1) {
                    System.out.println("Appointment booking cancelled.");
                    return;
                } else if (doctorIndex >= 0 && doctorIndex < clinic.getDoctors().size()) {
                    doctor = clinic.getDoctors().get(doctorIndex);
                } else {
                    System.out.println("Invalid doctor selection. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }

        LocalDate date;
        List<LocalTime> availableSlots;

        while (true) {
            System.out.println("Available Days for " + doctor.getName() + ":");
            List<DayOfWeek> availableDays = doctor.getAvailableDays();
            for (DayOfWeek day : availableDays) {
                System.out.println(day);
            }

            System.out.print("Enter Date (YYYY-MM-DD): ");
            String dateInput = scanner.nextLine();
            try {
                date = LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
                continue;
            }

            DayOfWeek dayOfWeek = date.getDayOfWeek();
            availableSlots = doctor.getAvailableSlots(dayOfWeek);

            if (!availableSlots.isEmpty()) {
                break;
            } else {
                System.out.println("Doctor is not available on " + dayOfWeek + ". Please choose an option:");
                System.out.println("1. Enter a different date");
                System.out.println("2. Cancel appointment booking");
                System.out.print("Select option (1 or 2): ");
                int option = scanner.nextInt();
                scanner.nextLine();

                if (option == 2) {
                    System.out.println("Appointment booking cancelled.");
                    return;
                }
            }
        }

        System.out.println("Available Time Slots for " + date + ":");
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.println((i + 1) + ". " + availableSlots.get(i));
        }

        LocalTime time;
        while (true) {
            System.out.print("Select Time Slot by number: ");
            int slotIndex = scanner.nextInt() - 1;
            if (slotIndex >= 0 && slotIndex < availableSlots.size()) {
                time = availableSlots.get(slotIndex);
                break;
            } else {
                System.out.println("Invalid time slot selection. Please try again.");
            }
        }

        Treatment treatment = null;
        while (treatment == null) {
            System.out.println("Available Treatments:");
            for (int i = 0; i < Treatment.values().length; i++) {
                System.out.println((i + 1) + ". " + Treatment.values()[i].name() + " - LKR " + Treatment.values()[i].getPrice());
            }
            System.out.print("Select Treatment by number, or enter 0 to cancel: ");

            if (scanner.hasNextInt()) {
                int treatmentIndex = scanner.nextInt() - 1;
                scanner.nextLine();

                if (treatmentIndex == -1) {
                    System.out.println("Appointment booking cancelled.");
                    return;
                } else if (treatmentIndex >= 0 && treatmentIndex < Treatment.values().length) {
                    treatment = Treatment.values()[treatmentIndex];
                } else {
                    System.out.println("Invalid treatment selection. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }

        System.out.println("To reserve this appointment, a registration fee of LKR 500 is required.");
        System.out.print("Do you want to pay the registration fee? (yes/no): ");
        String confirmPayment = scanner.nextLine().trim().toLowerCase();

        if (!confirmPayment.equals("yes")) {
            System.out.println("Appointment reservation cancelled.");
            return;
        }

        Appointment appointment = clinic.bookAppointment(patient, doctor, treatment, date, time);
        System.out.println("Appointment booked successfully with ID: " + appointment.getAppointmentId());
    }


    // Method to validate input using regex
    private static String getInputWithValidation(String prompt, String regex, String errorMessage) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (Pattern.matches(regex, input)) {
                break;
            } else {
                System.out.println(errorMessage);
            }
        }
        return input;
    }

    private static void updateAppointment() {
        System.out.print("Enter Appointment ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        Appointment appointment = clinic.searchAppointmentById(id);

        if (appointment == null) {
            System.out.println("Appointment not found.");
            return;
        }

        System.out.println("What would you like to update?");
        System.out.println("1. Update Patient Details");
        System.out.println("2. Update Appointment Date and Time");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1 -> updatePatientDetails(appointment.getPatient());
            case 2 -> updateAppointmentDateAndTime(appointment);
            default -> System.out.println("Invalid choice. Update cancelled.");
        }
    }

    private static void updatePatientDetails(Patient patient) {
        System.out.println("Updating Patient Details");

        System.out.print("Enter New Patient Name: ");
        String name = scanner.nextLine();
        patient.setName(name);

        String email = getInputWithValidation(
                "Enter New Patient Email: ",
                "^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$",
                "Invalid email format. Please try again."
        );
        patient.setEmail(email);

        String phone = getInputWithValidation(
                "Enter New Patient Phone (10 digits): ",
                "^[0-9]{10}$",
                "Invalid phone format. Please enter 10 digits."
        );
        patient.setPhone(phone);

        System.out.println("Patient details updated successfully.");
    }

    private static void updateAppointmentDateAndTime(Appointment appointment) {
        System.out.println("Updating Appointment Date and Time");

        LocalDate newDate = null;
        List<LocalTime> availableSlots = null;

        while (true) {
            System.out.print("Enter New Date (YYYY-MM-DD): ");
            String dateInput = scanner.nextLine();
            try {
                newDate = LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please try again.");
                continue;
            }

            // Get doctor's availability for the new date
            Doctor doctor = appointment.getDoctor();
            DayOfWeek dayOfWeek = newDate.getDayOfWeek();
            availableSlots = doctor.getAvailableSlots(dayOfWeek);

            if (!availableSlots.isEmpty()) {
                break;
            } else {
                System.out.println("Doctor is not available on " + dayOfWeek + ". Please try a different date.");
            }
        }

        System.out.println("Available Time Slots for " + newDate + ":");
        for (int i = 0; i < availableSlots.size(); i++) {
            System.out.println((i + 1) + ". " + availableSlots.get(i));
        }

        LocalTime newTime = null;
        while (true) {
            System.out.print("Select New Time Slot by number: ");
            int slotIndex = scanner.nextInt() - 1;
            if (slotIndex >= 0 && slotIndex < availableSlots.size()) {
                newTime = availableSlots.get(slotIndex);
                break;
            } else {
                System.out.println("Invalid time slot selection. Please try again.");
            }
        }

        appointment.setDate(newDate);
        appointment.setTime(newTime);
        System.out.println("Appointment date and time updated successfully.");
    }


    private static void viewAppointmentsByDay() {
        System.out.println("Select a day to view appointments:");
        for (DayOfWeek day : DayOfWeek.values()) {
            System.out.println((day.getValue()) + ". " + day);
        }

        System.out.print("Enter the day number (1 for Monday, 7 for Sunday): ");
        int dayChoice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        if (dayChoice < 1 || dayChoice > 7) {
            System.out.println("Invalid day selection. Please try again.");
            return;
        }

        DayOfWeek selectedDay = DayOfWeek.of(dayChoice);
        List<Appointment> appointments = clinic.viewAppointmentsByDay(selectedDay);

        if (appointments.isEmpty()) {
            System.out.println("No appointments found for " + selectedDay + ".");
        } else {
            System.out.println("Appointments on " + selectedDay + ":");
            for (Appointment appointment : appointments) {
                System.out.println("ID: " + appointment.getAppointmentId() +
                        ", Patient: " + appointment.getPatientName() +
                        ", Date: " + appointment.getDate() +
                        ", Time: " + appointment.getTime());
            }
        }
    }


    private static void searchAppointment() {
        System.out.print("Search by (1) Name or (2) ID: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // consume newline

        if (choice == 1) {
            System.out.print("Enter Patient Name: ");
            String name = scanner.nextLine();
            List<Appointment> results = clinic.searchAppointmentByName(name);
            if (results.isEmpty()) {
                System.out.println("No appointments found for this name.");
            } else {
                for (Appointment appointment : results) {
                    System.out.println("ID: " + appointment.getAppointmentId() +
                            ", Date: " + appointment.getDate() +
                            ", Time: " + appointment.getTime() +
                            ", Treatment: " + appointment.getTreatment().name());
                }
            }
        } else if (choice == 2) {
            System.out.print("Enter Appointment ID: ");
            int id = scanner.nextInt();
            Appointment appointment = clinic.searchAppointmentById(id);
            if (appointment != null) {
                System.out.println("Appointment Details:");
                System.out.println("ID: " + appointment.getAppointmentId());
                System.out.println("Patient: " + appointment.getPatientName());
                System.out.println("Date: " + appointment.getDate());
                System.out.println("Time: " + appointment.getTime());
                System.out.println("Treatment: " + appointment.getTreatment().name());
            } else {
                System.out.println("Appointment not found.");
            }
        }
    }

    private static void generateInvoice() {
        System.out.print("Enter Appointment ID for Invoice: ");
        int id = scanner.nextInt();
        Appointment appointment = clinic.searchAppointmentById(id);

        if (appointment == null) {
            System.out.println("Appointment not found.");
            return;
        }

        String invoice = Invoice.generateInvoice(appointment);
        System.out.println(invoice);
    }
}
