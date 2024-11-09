public class Invoice {
    private static final double TAX_RATE = 0.025;

    public static double calculateTotal(Treatment treatment) {
        double basePrice = treatment.getPrice();
        double tax = basePrice * TAX_RATE;
        return Math.round((basePrice + tax) * 100.0) / 100.0;
    }

    public static String generateInvoice(Appointment appointment) {
        double basePrice = appointment.getTreatment().getPrice();
        double totalPrice = calculateTotal(appointment.getTreatment());

        return "Invoice:\n" +
                "Appointment ID: " + appointment.getAppointmentId() + "\n" +
                "Patient: " + appointment.getPatientName() + "\n" +
                "Treatment: " + appointment.getTreatment().name() + "Treatment" + "\n" +
                "Base Price: LKR " + basePrice + "\n" +
                "Tax (2.5%): LKR " + (basePrice * TAX_RATE) + "\n" +
                "Total Amount: LKR " + totalPrice;
    }
}
