package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.PageHolder;
import kg.attractor.bookingsaas.dto.mapper.impl.PageHolderWrapper;
import kg.attractor.bookingsaas.dto.user.OutputUserDto;
import kg.attractor.bookingsaas.dto.user.UpdateUserDto;
import kg.attractor.bookingsaas.dto.mapper.OutputUserMapper;
import kg.attractor.bookingsaas.dto.mapper.UpdateUserMapper;
import kg.attractor.bookingsaas.models.User;
import kg.attractor.bookingsaas.repository.UserRepository;
import kg.attractor.bookingsaas.service.BusinessValidator;
import kg.attractor.bookingsaas.service.ServiceValidator;
import kg.attractor.bookingsaas.service.UserService;
import kg.attractor.bookingsaas.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UpdateUserMapper updateUserMapper;
    private final OutputUserMapper outputUserMapper;
    private final PageHolderWrapper pageHolderWrapper;
    private final BusinessValidator businessValidator;
    private final ServiceValidator serviceValidator;

    @Override
    public boolean isUserEmailIsUnique(String email) {
        if (email == null || email.isBlank())
            return false;

        return !userRepository.existsByEmail(email);
    }

    @Override
    public boolean isUserPhoneNumberUnique(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank())
            return false;

        var userByPhoneNumber = userRepository.findUserByPhone(phoneNumber);
        return userByPhoneNumber.isEmpty() || getAuthUser().getPhone().equals(phoneNumber);
    }

    @Override
    public User getAuthUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public UpdateUserDto updateUser(UpdateUserDto updateUserDto) throws IOException {
        User user = userRepository.findUserById(updateUserDto.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        var authUser = getAuthUser();
        if (!authUser.getId().equals(user.getId()))
            throw new IllegalArgumentException("Auth user " + authUser.getFirstName() + " cannot update " + updateUserDto.getFirstName());

        if (updateUserDto.getImage() != null) {
            FileUtil.deleteFile(user.getLogo());
            uploadUserFile(updateUserDto.getImage(), user);
        }
        updateUserMapper.updateUser(user, updateUserDto);
        return updateUserMapper.mapToDto(user);
    }

    private static void uploadUserFile(MultipartFile multipartFile, User user) throws IOException {
        String filePath = FileUtil.uploadFile(multipartFile);
        user.setLogo(filePath);
    }

    @Override
    public List<OutputUserDto> findClientsByBusinessId(Long businessId) {
        return userRepository.findUsersByBusinessId(businessId)
                .stream()
                .map(outputUserMapper::mapToDto)
                .toList();
    }

    @Override
    public PageHolder<OutputUserDto> getUsersByBusinessTitle(String businessTitle, int page, int size) {
        Assert.isTrue(businessTitle != null && !businessTitle.isBlank(), "Business title must not be null or blank");
        businessValidator.checkIsBusinessBelongsToAuthUser(businessTitle);
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
        Page<OutputUserDto> users = userRepository.findUserByBusinessTitle(businessTitle, pageable)
                .map(outputUserMapper::mapToDto);
        return pageHolderWrapper.wrapPageHolder(users);
    }

    @Override
    public PageHolder<OutputUserDto> findUsersByServiceId(Long serviceId, int page, int size) {
        Assert.notNull(serviceId, "Service ID must not be null");
        serviceValidator.checkServiceBelongsToAuthUser(serviceId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
        Page<OutputUserDto> clients = userRepository.findUsersByServiceId(serviceId, pageable)
                .map(outputUserMapper::mapToDto);
        return pageHolderWrapper.wrapPageHolder(clients);
    }
}
