public enum Treatment {
    ACNE(2750.00),
    WHITENING(7650.00),
    MOLE_REMOVAL(3850.00),
    LASER(12500.00);

    private final double price;

    Treatment(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
