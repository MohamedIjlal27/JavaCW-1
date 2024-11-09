import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Doctor {
    private String name;
    private Map<DayOfWeek, List<LocalTime>> schedule;

    public Doctor(String name) {
        this.name = name;
        this.schedule = new EnumMap<>(DayOfWeek.class);
        initializeSchedule();
    }

    private void initializeSchedule() {
        schedule.put(DayOfWeek.MONDAY, createTimeSlots("10:00", "13:00"));
        schedule.put(DayOfWeek.WEDNESDAY, createTimeSlots("14:00", "17:00"));
        schedule.put(DayOfWeek.FRIDAY, createTimeSlots("16:00", "20:00"));
        schedule.put(DayOfWeek.SATURDAY, createTimeSlots("09:00", "13:00"));
    }

    private List<LocalTime> createTimeSlots(String start, String end) {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime slotStart = LocalTime.parse(start);
        LocalTime slotEnd = LocalTime.parse(end);

        while (slotStart.isBefore(slotEnd)) {
            slots.add(slotStart);
            slotStart = slotStart.plusMinutes(15);
        }
        return slots;
    }

    // Method to get available time slots for a specific day
    public List<LocalTime> getAvailableSlots(DayOfWeek day) {
        return schedule.getOrDefault(day, new ArrayList<>());
    }

    public String getName() {
        return name;
    }

    // This method will be useful for displaying available days in the user interface
    public List<DayOfWeek> getAvailableDays() {
        List<DayOfWeek> availableDays = new ArrayList<>();
        for (Map.Entry<DayOfWeek, List<LocalTime>> entry : schedule.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                availableDays.add(entry.getKey());
            }
        }
        return availableDays;
    }

    // Method to get the full schedule
    public Map<DayOfWeek, List<LocalTime>> getSchedule() {
        return schedule;
    }

    // Method to get all available slots across all days
    public List<LocalTime> getAvailableSlots() {
        List<LocalTime> allAvailableSlots = new ArrayList<>();
        for (List<LocalTime> slots : schedule.values()) {
            allAvailableSlots.addAll(slots);
        }
        return allAvailableSlots;
    }
}
