package kg.attractor.bookingsaas.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBookQuantityDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long booksQuantity;

    public UserBookQuantityDto(Long id, String firstName, String lastName, String email, Long booksQuantity) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.booksQuantity = booksQuantity;
    }
}
