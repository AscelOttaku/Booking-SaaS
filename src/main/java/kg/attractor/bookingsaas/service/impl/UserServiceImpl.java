package kg.attractor.bookingsaas.service.impl;

import kg.attractor.bookingsaas.dto.UpdateUserDto;
import kg.attractor.bookingsaas.dto.mapper.UpdateUserMapper;
import kg.attractor.bookingsaas.models.User;
import kg.attractor.bookingsaas.repository.UserRepository;
import kg.attractor.bookingsaas.service.UserService;
import kg.attractor.bookingsaas.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UpdateUserMapper updateUserMapper;

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
}
