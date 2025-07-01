package kg.attractor.bookingsaas.service;

public interface BusinessValidator {
    void checkIsBusinessBelongsToAuthUser(Long businessId);

    void checkIsBusinessBelongsToAuthUser(String businessTitle);

    void checkIfBusinessExistByTitle(String businessTitle);
}
