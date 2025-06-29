package kg.attractor.bookingsaas.service;

public interface BusinessValidator {
    void checkIsBusinessBelongsToAuthUser(Long businessId);

    void checkIfBusinessExistByTitle(String businessTitle);
}
