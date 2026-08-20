package com.railbook.userservice.service;

import com.railbook.userservice.dto.CreateUserRequest;
import com.railbook.userservice.dto.UpdateUserRequest;
import com.railbook.userservice.dto.UserResponse;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUser(Long id);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void deleteUser(Long id);
}
