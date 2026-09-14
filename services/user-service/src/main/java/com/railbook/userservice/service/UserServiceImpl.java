package com.railbook.userservice.service;

import com.railbook.userservice.dto.CreateUserRequest;
import com.railbook.userservice.dto.UpdateUserRequest;
import com.railbook.userservice.dto.UserResponse;
import com.railbook.userservice.entity.User;
import com.railbook.userservice.exception.DuplicateUserException;
import com.railbook.userservice.exception.UserNotFoundException;
import com.railbook.userservice.repository.UserRepository;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Keycloak keycloakAdmin;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {

        // 1. Validate uniqueness in local DB
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

        UsersResource usersResource = keycloakAdmin.realm(realm).users();

        // 2. Prepare Keycloak UserRepresentation
        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(request.email());
        kcUser.setEmail(request.email());
        kcUser.setFirstName(request.firstName());
        kcUser.setLastName(request.lastName());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        // 3. Set User Password
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.password());
        credential.setTemporary(false);
        kcUser.setCredentials(Collections.singletonList(credential));

        // 4. Create User in Keycloak
        Response response = usersResource.create(kcUser);
        if (response.getStatus() != 201) {
            throw new RuntimeException(
                    "Failed to create Keycloak account. HTTP Status: " + response.getStatus()
            );
        }

        // Extract generated Keycloak ID from Location header URL
        String keycloakUserId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        try {
            // 5. Assign 'USER' Realm Role in Keycloak
            RoleRepresentation userRole = keycloakAdmin.realm(realm)
                    .roles()
                    .get("USER")
                    .toRepresentation();
            usersResource.get(keycloakUserId).roles().realmLevel().add(Collections.singletonList(userRole));

            // 6. Save User in Local Database (without password)
            User user = User.builder()
                    .firstName(request.firstName())
                    .lastName(request.lastName())
                    .email(request.email())
                    .phone(request.phone())
                    .keycloakUserId(keycloakUserId)
                    .build();

            return toResponse(userRepository.save(user));

        } catch (Exception ex) {
            // COMPENSATION: Delete the Keycloak user if DB operation or role assignment fails
            usersResource.get(keycloakUserId).remove();
            throw new RuntimeException("Registration failed, rolling back Keycloak user creation", ex);
        }
    }

    @Override
    public UserResponse getUser(Long id) {
        return userRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(String.valueOf(id)));

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

        String newFirstname = user.getFirstName();
        String newLastname = user.getLastName();
        String newEmail = user.getEmail();

        if (request.firstName() != null && !request.firstName().isBlank()) {
            newFirstname = request.firstName();
        }
        if (request.lastName() != null && !request.lastName().isBlank()) {
            newLastname = request.lastName();
        }

        if (request.email() != null && !request.email().isBlank()) {
            newEmail = request.email();
        }

        // Update Keycloak identity data
        UserRepresentation kcUser = new UserRepresentation();

        kcUser.setUsername(newEmail);
        kcUser.setEmail(newEmail);
        kcUser.setFirstName(newFirstname);
        kcUser.setLastName(newLastname);

        keycloakAdmin
                .realm(realm)
                .users()
                .get(user.getKeycloakUserId())
                .update(kcUser);

        // Update local profile
        user.setFirstName(newFirstname);
        user.setLastName(newLastname);
        user.setEmail(newEmail);

        if (request.phone() != null && !request.phone().isBlank()) {
            user.setPhone(request.phone());
        }

        return toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));

        // Delete from Keycloak first, then delete local record
        keycloakAdmin.realm(realm).users().get(user.getKeycloakUserId()).remove();
        userRepository.delete(user);
    }

    public UserResponse getUserByKeycloakId(String keycloakUserId) {
        return userRepository.findByKeycloakUserId(keycloakUserId)
                .map(this::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User profile not found for Keycloak Id: "+keycloakUserId));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}