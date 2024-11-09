public class Patient {
    private String nic;
    private String name;
    private String email;
    private String phone;

    // Constructor
    public Patient(String nic, String name, String email, String phone) {
        this.nic = nic;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getter Methods
    public String getName() {
        return name;
    }

    public String getNic() {
        return nic;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    // Setter Methods
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Optional: Override toString method for better representation
    @Override
    public String toString() {
        return "Patient{" +
                "nic='" + nic + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
