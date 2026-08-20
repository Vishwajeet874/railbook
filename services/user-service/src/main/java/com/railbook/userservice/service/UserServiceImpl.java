package com.railbook.userservice.service;

import com.railbook.userservice.dto.CreateUserRequest;
import com.railbook.userservice.dto.UpdateUserRequest;
import com.railbook.userservice.dto.UserResponse;
import com.railbook.userservice.entity.User;
import com.railbook.userservice.exception.DuplicateUserException;
import com.railbook.userservice.exception.UserNotFoundException;
import com.railbook.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateUserException(
                    "User already exists with email: " + request.email()
            );
        }

        if (userRepository.existsByPhone(request.phone())) {
            throw new DuplicateUserException(
                    "User already exists with phone: " + request.phone()
            );
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .password(request.password())
                .build();

        return toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getUser(Long id) {
        return userRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (request.email() != null
                && !request.email().equals(user.getEmail())
                && userRepository.existsByEmail(request.email())) {
            throw new DuplicateUserException(
                    "User already exists with email: " + request.email()
            );
        }

        if (request.phone() != null
                && !request.phone().equals(user.getPhone())
                && userRepository.existsByPhone(request.phone())) {
            throw new DuplicateUserException(
                    "User already exists with phone: " + request.phone()
            );
        }

        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name());
        }

        if (request.email() != null && !request.email().isBlank()) {
            user.setEmail(request.email());
        }

        if (request.phone() != null && !request.phone().isBlank()) {
            user.setPhone(request.phone());
        }

        return toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
