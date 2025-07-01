// ...existing code...
public OutputUserDto(String firstName, String middleName, String lastName, String phone, String email, String imagePath, String roleName) {
    super(firstName, middleName, lastName, phone); // Use this if the superclass has such a constructor
    // If not, use the setters as before:
    // super.setFirstName(firstName);
    // super.setMiddleName(middleName);
    // super.setLastName(lastName);
    // super.setPhone(phone);
    this.email = email;
    this.imagePath = imagePath;
    this.roleName = roleName;
}
// ...existing code...
