import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private static int idCounter = 1;
    private int appointmentId;
    private Patient patient;
    private Doctor doctor;
    private Treatment treatment;
    private LocalDate date;
    private LocalTime time;
    private boolean isPaid;

    public Appointment(Patient patient, Doctor doctor, Treatment treatment, LocalDate date, LocalTime time) {
        this.appointmentId = idCounter++;
        this.patient = patient;
        this.doctor = doctor;
        this.treatment = treatment;
        this.date = date;
        this.time = time;
        this.isPaid = false;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    // Add a method to return the patient's name
    public String getPatientName() {
        return patient.getName();
    }

    // Mark as paid
    public void markAsPaid() {
        this.isPaid = true;
    }

    // Get the doctor for this appointment
    public Doctor getDoctor() {
        return doctor;
    }

    // Add getter for patient
    public Patient getPatient() {
        return patient;
    }
}
