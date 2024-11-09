import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Clinic {
    private List<Doctor> doctors;
    private List<Appointment> appointments;

    public Clinic() {
        this.doctors = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public Appointment bookAppointment(Patient patient, Doctor doctor, Treatment treatment, LocalDate date, LocalTime time) {
        Appointment newAppointment = new Appointment(patient, doctor, treatment, date, time);
        appointments.add(newAppointment);
        return newAppointment;
    }

    // Updated method to view appointments by the day of the week
    public List<Appointment> viewAppointmentsByDay(DayOfWeek day) {
        List<Appointment> filteredAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDate().getDayOfWeek().equals(day)) {
                filteredAppointments.add(appointment);
            }
        }
        return filteredAppointments;
    }

    public Appointment searchAppointmentById(int id) {
        return appointments.stream()
                .filter(appointment -> appointment.getAppointmentId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Appointment> searchAppointmentByName(String name) {
        List<Appointment> filteredAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatientName().equalsIgnoreCase(name)) {
                filteredAppointments.add(appointment);
            }
        }
        return filteredAppointments;
    }
}
